package com.eb.modules.dao;

import com.alibaba.fastjson.JSONObject;
import com.eb.modules.model.Product;
import com.eb.modules.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Map;

//@Repository
public interface ProductDao extends MongoRepository<Product, String> {

    @Query(value = "{'productName':{$regex:?0}}", fields = "{}", sort = "{}") // 查找所有内容
    public Page<Product> getByPage(Pageable pageable, String productName);

    // 筛选指定内容
    // @Query(value = "{'classify': ?0.classify}", fields = "{}", sort = "{}")
    @Query(value = "{'classify': ?0}", fields = "{}", sort = "{}")
    public List<Product> getByClassify(String classify);

}
