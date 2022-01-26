package com.eb.framework.shiro;


import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;

/**
 * @ClassName ShiroSessionIdGenerator
 * @Description 自定义SessionId生成器
 * @Author
 * @Date 2020/5/7 20:49
 * @Version
 **/
public class ShiroSessionIdGenerator implements SessionIdGenerator {
    /**
     * @return java.io.Serializable
     * @MethodName generateId
     * @Description 实现SessionId生成
     * @Param [session]
     **/
    @Override
    public Serializable generateId(Session session) {
        Serializable sessionId = new JavaUuidSessionIdGenerator().generateId(session);
        System.out.print("自定义生成的sessionId:" + sessionId);
        return String.format("login_token_%s", sessionId);
    }
}