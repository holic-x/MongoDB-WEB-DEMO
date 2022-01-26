package com.eb.modules.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eb.framework.utils.ConstUtil;
import com.eb.framework.utils.CommonUtil;
import com.eb.framework.utils.MongoDBUtil;
import com.eb.modules.dao.UserDao;
import com.eb.modules.model.User;
import com.eb.modules.service.UserService;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 1.传统db操作实现mongodb处理
 * 2.借助MongoRepository封装db操作处理
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /*
    @Override
    public boolean edit(User user) {
        // 原生db操作测试
        String userId = user.getUserId();
//        String userId = user.get_id();
        if (StringUtils.isEmpty(userId)) {
            // 执行新增操作
            user.setUserId(CommonUtil.getRandomId());
            Map<String, Object> userMap = JSON.parseObject(JSONObject.toJSONString(user), Map.class);
            MgDbUtil.add(userMap, ConstUtil.TB_USER_INFO);
        } else {
            // 执行修改操作
//            Map<String, Object> whereMap, Map<String, Object> valueMap, String collectionName
            Map<String, Object> whereMap = new HashMap<>();
            // MgDbUtil修改操作无效(待调整，此处借助MongoClient相关工具类进行操作)
//            whereMap.put("_id",userId);
            // whereMap.put("userName",user.getUserName());
            // MgDbUtil.updateSet(whereMap,JSON.parseObject(JSONObject.toJSONString(user),Map.class),ConstUtil.TB_USER_INFO);
            // 批量修改操作
            Map<String, Object> conditionParams = Maps.newHashMap();
            conditionParams.put("userId", userId);
            MongoDBUtil mongoDBUtil = MongoDBUtil.getMongoDBUtilInstance();
            // localhost:27017 eb_db
            MongoClient defaultClient = MongoDBUtil.getMongoClientByCredential("127.0.0.1", 27017, "myuser", "eb_db", "000000");
            MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(defaultClient, "eb_db", "user_info");
            MongoDBUtil.getMongoDBUtilInstance().updateDocument(collection, conditionParams, JSON.parseObject(JSONObject.toJSONString(user), Map.class), false);
        }
        return false;
    }
    */
    @Override
    public boolean edit(User user) {
        // 原生db操作测试
        String userId = user.getUserId();
        Timestamp currentTime = CommonUtil.getCurrentTimestamp();
        if (StringUtils.isEmpty(userId)) {
            // 执行新增操作
            user.setUserId(CommonUtil.getRandomId());
            user.setCreateBy("");
            user.setModifyBy("");
            user.setCreateTime(currentTime);
            user.setModifyTime(currentTime);
            userDao.save(user);
        } else {
            // 执行修改操作
            user.setModifyBy("");
            user.setCreateTime(currentTime);
            user.setModifyTime(currentTime);
            userDao.save(user);
        }
        return true;
    }



    @Override
    public boolean delete(List<String> userIdList) {
       // MgDbUtil.delete(userId, ConstUtil.TB_USER_INFO);
        for(String userId : userIdList){
            userDao.deleteById(userId);
        }
        return true;
    }

    @Override
    public User getById(String userId) {
        Optional<User> user = userDao.findById(userId);
        return user!=null?user.get():null;
    }

    @Override
    public Page<User> getByPage(JSONObject queryCond) {
        // 指定分页查找参数（默认分页注意集合的越界处理）,currentPage做-1处理
        // page:指定页数 limit：分页大小
        Pageable pageable = PageRequest.of(queryCond.getInteger("page")-1, queryCond.getInteger("limit"));

        Page<User> pageData = userDao.getByPage(pageable, queryCond.getString("userName"));
//        PageHelper<User> pageHelper = new PageHelper<User>(pageData.getNumber(), pageData.getSize(), pageData.getTotalPages(), pageData.getTotalElements(), pageData.getContent());
        return pageData;

        // 使用自定义工具类进行封装(sort不能为null)
//        SpringDataPageAble springDataPageAble = new SpringDataPageAble(queryCond.getInteger("currPage"),queryCond.getInteger("pageSize"),null);
//        return userDao.getByPage(springDataPageAble,queryCond);
    }

    @Override
    public boolean changeUserStatus(JSONObject updateContent) {
        String userId = updateContent.getString("userId");
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        Map<String, Object> conditionParams = Maps.newHashMap();
        conditionParams.put("userId", userId);
        MongoCollection<Document> collection = MongoDBUtil.getMongoDBUtilInstance().getMongoCollection(ConstUtil.DEFAULT_CLIENT, "eb_db", "user_info");
        MongoDBUtil.getMongoDBUtilInstance().updateDocument(collection, conditionParams, JSON.parseObject(JSONObject.toJSONString(updateContent), Map.class), false);
        return true;
    }

    @Override
    public boolean updateLoginPwd(String userId, String oldPwd, String newPwd) {
        User user =  getById(userId);
        if(user == null){
            return false;
        }else{
            // 校验密码是否正常
            if(!oldPwd.equals(user.getPassword())){
                return false;
            }
            // 修改密码
            User updateUser = new User();
            updateUser.setUserId(userId);
            updateUser.setPassword(newPwd);
            edit(updateUser);
        }
        return true;
    }
}
