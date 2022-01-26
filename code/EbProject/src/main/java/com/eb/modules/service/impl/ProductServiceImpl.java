package com.eb.modules.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.eb.framework.utils.CommonUtil;
import com.eb.modules.dao.ProductDao;
import com.eb.modules.model.Product;
import com.eb.modules.model.User;
import com.eb.modules.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public boolean edit(Product product) {
        String productId = product.getProductId();
        Timestamp currentDate = CommonUtil.getCurrentTimestamp();
        if (StringUtils.isEmpty(productId)) {
            // 新增操作
            product.setProductId(CommonUtil.getRandomId());
            product.setCreateBy("");
            product.setModifyBy("");
            product.setCreateTime(currentDate);
            product.setModifyTime(currentDate);
//            productDao.insert(product);
            productDao.save(product);
        } else {
            // 编辑操作
            product.setModifyBy("");
            product.setModifyTime(currentDate);
            productDao.save(product);
        }
        return false;
    }

    @Override
    public boolean delete(List<String> productIdList) {
        for (String productId : productIdList) {
            productDao.deleteById(productId);
        }
        return true;
    }

    @Override
    public Map<String, Object> getById(String productId) {
        Optional<Product> findProduct = productDao.findById(productId);
        // 将Product对象转化为Map集合
        return findProduct != null ? JSON.parseObject(JSON.toJSONString(findProduct.get()), new TypeReference<Map<String, Object>>() {
        }) : null;
    }

    @Override
    public Page<Product> getByPage(JSONObject queryCond) {
        Pageable pageable = PageRequest.of(queryCond.getInteger("page") - 1, queryCond.getInteger("limit"));
        return productDao.getByPage(pageable, queryCond.getString("productName"));
    }

    @Override
    public List<Product> getByCond(JSONObject queryCond) {
        List<Product> resList = productDao.getByClassify(queryCond.getString("classify"));
        return resList;
    }

}
