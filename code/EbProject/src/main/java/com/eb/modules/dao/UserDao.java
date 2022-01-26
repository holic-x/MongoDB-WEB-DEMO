package com.eb.modules.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eb.modules.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

//@Repository
public interface UserDao extends MongoRepository<User,String> {

    // 分页数据处理(pageable不纳入筛选条件处理，虽然此处queryCond在第二个参数位置，但其算是第一个参数)
//    @Query(value = "{'userName.type':?0.userName.type}" ,fields = "{}",sort = "{}")  语句动态解析无效
//    public Page<User> getByPage(Pageable pageable, JSONObject queryCond);

    @Query(value = "{'userName':{$regex:?0}}" ,fields = "{}",sort = "{}") // 查找所有内容
    public Page<User> getByPage(Pageable pageable, String userName);

    @Query(value = "{'userName':{$regex:?0}}" ,fields = "{}",sort = "{}") // 查找所有内容
    public User getByName(String userName);

}
