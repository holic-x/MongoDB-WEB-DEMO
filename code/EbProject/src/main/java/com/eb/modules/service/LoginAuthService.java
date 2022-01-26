package com.eb.modules.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * 登录权限相关Service
 **/
public interface LoginAuthService {

    /**
     * 登录入口
     **/
    public Map<String,Object> toLogin(String loginAccount,String loginPassword);

    /**
     * 验证登录信息
     **/
    public JSONObject validLoginInfo(String loginAccount, String loginPassword);

    /**
     * 获取关联的角色、权限信息列表封装为对应Map<String, Set<String>>数据
     **/
    public Map<String, Set<String>> getRelateAuth();

}
