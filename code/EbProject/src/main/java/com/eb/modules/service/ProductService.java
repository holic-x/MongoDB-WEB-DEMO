package com.eb.modules.service;


import com.alibaba.fastjson.JSONObject;
import com.eb.modules.model.Product;
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
public interface ProductService {

    /**
     * 添加/修改商品信息(根据指定的productId进行判断)
     **/
    public boolean edit(Product product);

    /**
     * 删除商品信息(单条或批量)
     **/
    public boolean delete(List<String> productIdList);

    /**
     * 根据商品id获取商品信息详情
     **/
     public Map<String,Object> getById(String productId);

    /**
     * 根据条件分页查找商品列表
     **/
    public Page<Product> getByPage(JSONObject queryCond);

    /**
     * 根据商品分类等参数获取商品信息
     */
    public List<Product> getByCond(JSONObject queryCond);

}
