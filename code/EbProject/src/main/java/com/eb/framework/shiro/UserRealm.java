package com.eb.framework.shiro;

import com.alibaba.fastjson.JSONObject;
import com.eb.framework.exception.EbException;
import com.eb.framework.utils.ConstUtil;
import com.eb.modules.service.LoginAuthService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName ShiroRealm
 * @Description 自定义认证规则
 * @Author
 * @Date 2020/5/7 20:50
 * @Version
 **/
public class UserRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private LoginAuthService loginAuthService;


    /**
     * 授权查询回调函数-进行鉴权但缓存中无用户的授权信息时调用
     *
     * @return org.apache.shiro.authz.AuthorizationInfo
     * @MethodName doGetAuthorizationInfo
     * @Description 用户进行权限验证时候Shiro会去缓存中找, 如果查不到数据, 会执行这个方法去查权限, 并放入缓存中
     * @Param [principalCollection]
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        log.info("shiro-授权认证");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // LoginUser loginUser = (LoginUser) principalCollection.getPrimaryPrincipal();

        Map<String, Set<String>> relateAuth = loginAuthService.getRelateAuth();

        // 自定义授权处理(角色、权限设定)
        Set<String> roleSet = new HashSet<>();
        Set<String> authoritySet = new HashSet<>();

        if (!CollectionUtils.isEmpty(relateAuth)) {
            // 处理封装好的数据
            roleSet = relateAuth.get(ConstUtil.LOGIN_RELATE_ROLE_SET);
            authoritySet = relateAuth.get(ConstUtil.LOGIN_RELATE_AUTH_SET);
        }

        // 将自定义查询的权限和角色分别传入authorizationInfo中
        authorizationInfo.setRoles(roleSet);
        // 设定权限:addStringPermission(); setStringPermissions(authoritySet)
        authorizationInfo.setStringPermissions(authoritySet);
        return authorizationInfo;

        // authorizationInfo.addStringPermission();  设定权限
        //将自定义查询的权限和角色分别传入authorizationInfo中
//        authorizationInfo.setStringPermissions(authoritySet);
//        authorizationInfo.setRoles(roleSet);
    }

    /**
     * @return AuthenticationInfo
     * @MethodName doGetAuthenticationInfo
     * @Description 认证回调函数-用户身份认证,在登录时触发调用
     * @Param [authenticationToken]
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("shiro-登录认证");

        // 如果需要配置自定义Token需要相应配置注入,否则报数据转换类型错误(可通过查看UsernamePasswordToken的类定义,了解每个方法的作用含义)
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String loginAccount = token.getUsername(); // 或者用token.getPrincipal()替代
        String loginPassword = new String(token.getPassword()); // token.getCredentials()(返回数据Object)

        // 实际项目中,可以根据实际情况做缓存,如果不做,Shiro自己也是有时间间隔机制,2分钟内不会重复执行该方法

        // 获取登录数据
        JSONObject loginUser = new JSONObject();
        try {
            loginUser = loginAuthService.validLoginInfo(loginAccount, loginPassword);
            if (loginUser == null) {
                throw new EbException("登录名或密码错误");
            }

            if (loginUser != null) {
                // 处理重复登录的session
                SessionsSecurityManager securityManager = (SessionsSecurityManager) SecurityUtils.getSecurityManager();
                DefaultSessionManager sessionManager = (DefaultSessionManager) securityManager.getSessionManager();
                // 获取当前已登录的用户session列表
                Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
                // 清除该用户以前登录时保存的session
                for (Session session : sessions) {
                    // 同一个浏览器登录:如果和当前session是同一个session，需要检验当前登录用户与此前的登录用户是否为同一个,踢出当前浏览器的上一个登录用户
                    if (SecurityUtils.getSubject().getSession().getId().equals(session.getId())) {
                        break;
                        /*
                        User currentUser = (User) (session.getAttribute("currentUser"));
                        if (currentUser != null) {
                            // 校验同一浏览器条件下是否登录不同用户的情况:用户编号或ID相同则认为是同一个用户
                            if (!currentUser.getUserNum().equals(loginUser.getUserNum())) {
                                // log.info("当前检测到新的用户登入"+currentUser.getUserName() + "被踢出...");
                                log.info("当前检测到新的用户登入:"+loginUser.getUserName());
                                // sessionManager.getSessionDAO().delete(session);
                            }
                        }
                        */
                    } else {
                        // 不同浏览器登录:校验同一用户重复登录的情况
                        // Map currentUser = JSON.parseObject((String)session.getAttribute("currentUser"),Map.class); 解决 不同类型用户实体转化异常(Admin\User)
                        JSONObject currentUser = (JSONObject) session.getAttribute(ConstUtil.LOGIN_USER_CACHE);
                        if (currentUser != null) {
                            // 校验不同浏览器条件下同一用户是否重复登录的情况:userId相同则认为是同一个用户
                            if (loginUser.getString("userId").equals(currentUser.getString("userId"))) {
                                log.info(currentUser.getString("userName") + "已在某处重复登录,剔除中...");
                                sessionManager.getSessionDAO().delete(session);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 捕获所有异常:异常处理转化,使异常信息正常并拦截并返回给前端
            throw new AccountException(e.getMessage());
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                loginUser,        // 传入整个登录用户实体对象随后权限认证的时候便可通过(T)principalCollection.getPrimaryPrincipal()访问
                loginPassword,    // 密码
//                ByteSource.Util.bytes(user.getSalt()), //设置盐值
                getName()
        );
        return authenticationInfo;
    }

}
