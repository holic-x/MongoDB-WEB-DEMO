package com.eb.framework.config;

import com.eb.framework.shiro.CustomFilterUtil;
import com.eb.framework.shiro.ShiroSessionIdGenerator;
import com.eb.framework.shiro.ShiroSessionManager;
import com.eb.framework.shiro.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfig
 * @Description TODO
 * @Author
 * @Date 2020/5/9 14:30
 * @Version
 **/
@Configuration
public class ShiroConfig {

    // 缓存相关配置
    private final String CACHE_KEY = "shiro:cache:";
    private final String SESSION_KEY = "shiro:session:";
    private final int EXPIRE = 1800;

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    // getDefaultAdvisorAutoProxyCreator、authorizationAttributeSourceAdvisor对注解权限起作用有很大的关系(放置在配置的最上面)
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }

    /**
     * 匹配所有加了Shiro 认证注解的方法(如果不添加则权限注解不生效)
     * 开启Shiro-aop注解支持:使用代理方式所以需要开启代码支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * @return com.sz.mip.framework.shiro.realm.ShiroRealm
     * @MethodName shiroRealm
     * @Description 自定义身份验证器-将自身的验证方式载入容器
     * @Param []
     **/
    @Bean
    public UserRealm shiroRealm() {
        UserRealm shiroRealm = new UserRealm();
//        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    /**
     * @return org.apache.shiro.spring.web.ShiroFilterFactoryBean
     * @MethodName shiroFilterFactory
     * @Description Shiro基础配置-Filter工厂，设置对应的过滤条件和跳转条件
     * @Param [securityManager]
     **/
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactory(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 注意过滤器配置顺序不能颠倒(如果颠倒可能导致访问路径拦截效果差强人意)

        // 配置过滤:不会被拦截的链接(如果加了接口访问失败,考虑拦截过滤的先后顺序)
//        filterChainDefinitionMap.put("/framework/rest/cm/noLogin", "logout");

        /**
         *  shiro拦截配置说明：
         *  anon:匿名拦截器，即不需要登录即可访问；一般用于静态资源过滤、登录入口
         *  authc:如果没有登录会跳到相应的登录页面登录
         *  user:用户拦截器，用户已经身份验证/记住我登录的都可访问
         **/
        filterChainDefinitionMap.put("/login/toLogin", "anon");
        // 获取指定工具类中自定义的过滤集合
        Map<String, String> anonMap = CustomFilterUtil.getAnonMap();
        filterChainDefinitionMap.putAll(anonMap);

        //配置退出过滤器,Shiro定义退出代码
        filterChainDefinitionMap.put("/login/toLogout", "logout");
        // 可以自定义登录退出 url,anon 拦截器定义
//        filterChainDefinitionMap.put("/afterlogout", "logout");

        // 对所有用户进行验证(所有url必须认证通过之后才可访问,一般将/**放置在拦截的最下方)
        filterChainDefinitionMap.put("/**", "authc");

        // 配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/login/noLogin");

        // 错误页面，认证不通过跳转(未授权页面跳转)
        shiroFilterFactoryBean.setUnauthorizedUrl("/login/loginFail");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * @return org.apache.shiro.mgt.SecurityManager
     * @MethodName securityManager
     * @Description 安全管理器-权限管理，配置主要是Realm的管理认证
     * @Param []
     **/
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 自定义的shiro session缓存管理器
        securityManager.setSessionManager(sessionManager());
        // 自定义Cache实现
        // securityManager.setCacheManager(cacheManager());
        // 自定义Realm验证(放到最后)
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }

    /**
     * 凭证匹配器
     * 将密码校验交给Shiro的SimpleAuthenticationInfo进行处理,在这里做匹配配置
     * @Author Sans
     * @CreateTime 2019/6/12 10:48
     */
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
//        // 散列算法:这里使用SHA256算法;
//        shaCredentialsMatcher.setHashAlgorithmName(SHA256Util.HASH_ALGORITHM_NAME);
//        // 散列的次数，比如散列两次，相当于 md5(md5(""));
//        shaCredentialsMatcher.setHashIterations(SHA256Util.HASH_ITERATIONS);
//        return shaCredentialsMatcher;
//    }

    /**
     * @return ShiroSessionIdGenerator
     * @MethodName sessionIdGenerator
     * @Description SessionID生成器 - 自定义session生成器
     * @Param []
     **/
    @Bean
    public ShiroSessionIdGenerator sessionIdGenerator() {
        return new ShiroSessionIdGenerator();
    }

    /**
     * @return org.apache.shiro.session.mgt.SessionManager
     * @MethodName sessionManager
     * @Description 自定义配置Session管理器
     * @Param []
     **/
    @Bean
    public SessionManager sessionManager() {
        // 注入自定义的shiro session
        ShiroSessionManager shiroSessionManager = new ShiroSessionManager();
        // 如果后续考虑多tomcat部署应用,则可使用shiro-redis开源插件作session的控制,或者是nginx负载均衡
//        shiroSessionManager.setSessionDAO(redisSessionDAO());  sessionDAO中设定自定义sessionId
        return shiroSessionManager;
    }

}