package com.eb.modules.service;


import com.alibaba.fastjson.JSONObject;
import com.eb.modules.model.Comment;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2020-08-11
 */
public interface CommentService {

    /**
     * 添加/修改评论信息(根据指定的commentId进行判断)
     **/
    public boolean edit(Comment comment);

    /**
     * 删除评论信息(单条或批量)
     **/
    public boolean delete(List<String> commentIdList);

    /**
     * 根据评论id获取评论信息详情
     **/
     public Map<String,Object> getById(String commentId);

    /**
     * 根据条件分页查找评论列表
     **/
    public Page<Comment> getByPage(JSONObject queryCond);
    public Page<Map<String,Object>> getByPageAndCond(JSONObject queryCond);

    /**
     * 根据筛选条件查找评论列表
     **/
    public List<Comment> getByCond(JSONObject queryCond);

}
