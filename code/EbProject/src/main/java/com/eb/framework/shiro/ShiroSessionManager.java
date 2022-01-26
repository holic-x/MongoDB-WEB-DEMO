package com.eb.framework.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @ClassName ShiroSessionManager
 * @Description 自定义获取token
 * @Author
 * @Date 2020/5/9 14:28
 * @Version
 **/
public class ShiroSessionManager extends DefaultWebSessionManager {

    // 定义常量(token对应的key):请求头中使用的标识key,用来传递token
    private static final String AUTH_TOKEN = "authToken";

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    // 重写构造器
    public ShiroSessionManager() {
        super();
        // 设定session失效时间(默认是30分钟)
        setGlobalSessionTimeout(MILLIS_PER_MINUTE * 100);
        this.setDeleteInvalidSessions(true);
    }

    /**
     * @return java.io.Serializable
     * @MethodName getSessionId
     * @Description 重写方法实现从请求头获取Token便于接口统一(每次请求进来, Shiro会去从请求头找Authorization这个key对应的Value ( Token))
     * @Param [request, response]
     **/
    @Override
    public Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String token = WebUtils.toHttp(request).getHeader(AUTH_TOKEN);
        // 获取请求头中的 AUTH_TOKEN 的值，如果请求头中有 AUTH_TOKEN 则其值为sessionId.shiro就是通过sessionId来控制
        if (!StringUtils.isEmpty(token)) {
            // 请求头中如果有 authToken, 则其值为sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            // sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return token;
        } else {
            // 如果没有携带id参数则按照父类的方式在cookie进行获取sessionId
            return super.getSessionId(request, response);
        }
    }
}