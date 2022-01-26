package com.eb.modules.dao;

import com.alibaba.fastjson.JSONObject;
import com.eb.modules.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

// @Repository
public interface CommentDao extends MongoRepository<Comment, String> {

    @Query(value = "{'content':{$regex:?0}}", fields = "{}", sort = "{}") // 查找所有内容
    public Page<Comment> getByPage(Pageable pageable, String content);

    // 根据筛选条件创建
//    @Query(value = "{'productId':?0,'commentor':?1}", fields = "{}", sort = "{'commentTime': -1}")
    @Query(value = "{'productId':?0}", fields = "{}", sort = "{'commentTime': -1}")
    public List<Comment> getByCond(String productId,String commentor);

}
