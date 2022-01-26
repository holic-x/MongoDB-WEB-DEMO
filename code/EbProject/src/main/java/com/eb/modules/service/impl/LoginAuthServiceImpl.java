package com.eb.modules.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eb.framework.exception.EbException;
import com.eb.framework.shiro.ShiroUtil;
import com.eb.framework.utils.ConstUtil;
import com.eb.modules.dao.UserDao;
import com.eb.modules.model.User;
import com.eb.modules.service.LoginAuthService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * LoginServiceImpl
 **/
@Service
public class LoginAuthServiceImpl implements LoginAuthService {

    private static final Logger logger = LoggerFactory.getLogger(LoginAuthServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public Map<String,Object> toLogin(String loginAccount,String loginPassword) {

        if(StringUtils.isEmpty(loginAccount)||StringUtils.isEmpty(loginPassword)){
            System.out.println("账号或者密码不能为空");
            return null;
        }

        // shiro验证登录(身份验证)
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginAccount, loginPassword);
        // 设定remember:oken.setRememberMe(false);
        subject.login(token);
        // 将数据存入session
        SecurityUtils.getSubject().getSession().setAttribute(ConstUtil.LOGIN_USER_CACHE, subject.getPrincipal());

        Map<String,Object> resultMap = new HashMap<String,Object>();
        // 封装登录令牌（Serializable）
        resultMap.put("token",subject.getSession().getId());
        // 获取登录后的用户信息(Object)
        resultMap.put("currentUser", ShiroUtil.getPrincipal());
        return resultMap;
    }



    @Override
    public JSONObject validLoginInfo(String loginAccount, String loginPassword) {
        // 账号验证失败统一处理为用户名或者密码错误
        User findUser = userDao.getByName(loginAccount);
        if(findUser!=null){
            // 校验用户身份信息
            if(!findUser.getPassword().equals(loginPassword)){
                return null;
            }
            return JSONObject.parseObject(JSON.toJSONString(findUser));
//        return JSONObject.parseObject(JSONObject.toJSONString(findUser));
        }
        return null;
    }

    @Override
    public Map<String, Set<String>> getRelateAuth() {
        Map<String, Set<String>> roleAuthMap = new HashMap<String, Set<String>>();
        // 获取关联绑定的角色、权限数据信息
        return roleAuthMap;
    }
}
