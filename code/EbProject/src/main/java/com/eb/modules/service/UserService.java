package com.eb.modules.service;


import com.alibaba.fastjson.JSONObject;
import com.eb.modules.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2020-08-11
 */
public interface UserService {

    /**
     * 添加/修改用户信息(根据指定的userId进行判断)
     **/
    public boolean edit(User user);

    /**
     * 删除用户信息
     **/
    public boolean delete(List<String> userIdList);

    /**
     * 根据用户id获取用户信息详情(Map<String,Object>)
     **/
     public User getById(String userId);

    /**
     * 根据条件分页查找用户列表
     **/
    public Page<User> getByPage(JSONObject queryCond);

    /**
     * 启用/禁用用户账号
     * 通过userId、userStatus设置用户账号状态（交互传入json对象）
     **/
    public boolean changeUserStatus(JSONObject updateContent);

    /**
     * 修改指定用户登录绵绵
     **/
    public boolean updateLoginPwd(String userId,String oldPwd,String newPwd);

}
