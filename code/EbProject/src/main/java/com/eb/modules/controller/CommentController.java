package com.eb.modules.controller;

import com.alibaba.fastjson.JSONObject;
import com.eb.framework.utils.PageHelper;
import com.eb.framework.utils.Res;
import com.eb.modules.model.Comment;
import com.eb.modules.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/sys/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 保存信息
     **/
    @RequestMapping("/save")
    public Res save(@RequestBody Comment comment) {
        commentService.edit(comment);
        return Res.ok();
    }

    /**
     * 删除信息(单条或批量删除)
     **/
    @RequestMapping("/delete")
    public Res delete(@RequestBody JSONObject requestParam) {
        commentService.delete(requestParam.getObject("commentIdList", ArrayList.class));
        return Res.ok();
    }

    /**
     * 更新信息
     **/
    @RequestMapping("/update")
    public Res update(@RequestBody Comment comment) {
        commentService.edit(comment);
        return Res.ok();
    }

    /**
     * list信息
     **/
    @RequestMapping("/list")
    public Res list(@RequestBody JSONObject queryCond) {
        // 方式1：借助MongoRepository进行分页操作
//        Page<Comment> pageData = commentService.getByPage(queryCond);
        // 借助分页插件转化分页数据
//        return Res.ok().put("page",new PageHelper<Comment>(pageData));

        // 方式2：借助MongoTemplate进行分页操作
        Page<Map<String,Object>> pageData = commentService.getByPageAndCond(queryCond);
        return Res.ok().put("page",new PageHelper<Map<String,Object>>(pageData));
    }

    /**
     * 获取信息
     **/
    @RequestMapping("/info")
    public Res info(@RequestBody JSONObject requestParam) {
        return Res.ok(commentService.getById(requestParam.getString("commentId")));
    }

    /**
     * 获取信息
     **/
    @RequestMapping("/getByCond")
    public Res getByCond(@RequestBody JSONObject requestParam) {
        return Res.ok().put("list",commentService.getByCond(requestParam));
    }

}
