package com.eb.framework.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

/** 自定义Shiro过滤工具类 **/
public class CustomFilterUtil {

    /**
     *  shiro拦截配置说明：
     *  anon:匿名拦截器，即不需要登录即可访问；一般用于静态资源过滤、登录入口
     *  authc:如果没有登录会跳到相应的登录页面登录
     *  user:用户拦截器，用户已经身份验证/记住我登录的都可访问
     **/
    private static String ANON = "anon";

    private static String LOGOUT = "logout";

    private static String AUTHC = "authc";

    /** 自定义过滤的链接MAP集合(注意路径和对应拦截器的处理顺序) **/
    public static Map<String, String> getAnonMap(){
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 匿名放行公共的接口
        filterChainDefinitionMap.put("/public/**", ANON);

        // 配置静态资源过滤
        filterChainDefinitionMap.put("/static/**", ANON);

        return filterChainDefinitionMap;

    }

}
