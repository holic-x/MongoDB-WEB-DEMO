package com.eb.modules.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eb.framework.utils.PageHelper;
import com.eb.framework.utils.Res;
import com.eb.modules.model.User;
import com.eb.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 保存信息
     **/
    @RequestMapping("/save")
    public Res save(@RequestBody User user) {
        userService.edit(user);
//        return AjaxResultUtil.success(); // return AjaxResult
        return Res.ok();
    }

    /**
     * 删除信息(单条或批量删除)
     **/
    @RequestMapping("/delete")
    public Res delete(@RequestBody JSONObject requestParam) {
        userService.delete(requestParam.getObject("userIdList", ArrayList.class));
        return Res.ok();
    }

    /**
     * 更新信息
     **/
    @RequestMapping("/update")
    public Res update(@RequestBody User user) {
        userService.edit(user);
        return Res.ok();
    }

    /**
     * list信息
     **/
    @RequestMapping("/list")
    // @RequestBody HashMap<String, String> map 转化JSONObject.parseObject(JSON.toJSONString(map))
    public Res list(@RequestBody JSONObject queryCond ) {
        Page<User> pageData = userService.getByPage(queryCond);
        // 借助分页插件转化分页数据
        return Res.ok().put("page",new PageHelper<User>(pageData));
    }

    /**
     * 获取信息
     **/
    @RequestMapping("/info/{userId}")
    public Res info(@PathVariable String userId) {
        User user = userService.getById(userId);
        return user!=null?Res.ok().put("user",user):Res.error("指定ID关联信息不存在");
    }

}
