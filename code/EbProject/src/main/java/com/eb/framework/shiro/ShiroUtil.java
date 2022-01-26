package com.eb.framework.shiro;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

/**
 * @ClassName ShiroUtil
 * @Description TODO
 * @Author
 * @Date 2020/5/7 20:44
 * @Version
 **/
public class ShiroUtil {

    /**
     * 私有构造器
     **/
    private ShiroUtil() {
    }


    /**
     * getSubject() : SecurityUtils.getSubject()
     **/
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }


    /**
     * 获取当前用户Session
     **/
    public static Session getSession() {
        return getSubject().getSession();
    }

    /**
     * 获取当前登录用户信息:getPrincipal() : SecurityUtils.getSubject(),用Object接收
     **/
    public static Object getPrincipal() {
        return SecurityUtils.getSubject().getPrincipal();
    }


    /**
     * 用户登出
     **/
    public static void logout() {
        getSubject().logout();
    }

    /**
     * 获取当前用户信息
     **/
    public static JSONObject getCurrentUser() {
//        Admin admin = null;
//        Object obj = getSubject().getPrincipal();
//        if (obj != null) {
//            admin = new Admin();
//            BeanUtils.copyBeanProp(admin, obj);
//        }
//        return admin;
//        return (BaseUser) getSubject().getPrincipal();
        // return new LoginUser();
        return (JSONObject)getSubject().getPrincipal();
    }


    /**
     * 删除用户缓存信息
     **/
    public static void deleteCache(String username, boolean isRemoveSession) {
        //从缓存中获取Session
        Session session = null;
//        LoginUser loginUser;
        Object attribute = null;

//        for(Session sessionInfo : sessions){
//            //遍历Session,找到该用户名称对应的Session
//            attribute = sessionInfo.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
//            if (attribute == null) {
//                continue;
//            }
//            sysUserEntity = (SysUserEntity) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
//            if (sysUserEntity == null) {
//                continue;
//            }
//            if (Objects.equals(sysUserEntity.getUsername(), username)) {
//                session=sessionInfo;
//            }
//        }
//
        if (session == null || attribute == null) {
            return;
        }
        //删除session
        if (isRemoveSession) {
//            redisSessionDAO.delete(session);
        }
        // 删除Cache，在访问受限接口时会重新授权
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        Authenticator authc = securityManager.getAuthenticator();
        ((LogoutAware) authc).onLogout((SimplePrincipalCollection) attribute);
    }

    /**
     * 获取当前登录ip数据
     **/
    public static String getIp() {
        return getSubject().getSession().getHost();
    }

    /**
     * 获取sessionId
     **/
    public static String getSessionId() {
        return String.valueOf(getSubject().getSession().getId());
    }

    /**
     * 生成随机盐
     */
    public static String randomSalt() {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        return hex;
    }
}
