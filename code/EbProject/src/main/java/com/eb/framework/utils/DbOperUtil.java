package com.eb.framework.utils;

import com.alibaba.fastjson.JSON;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Map;

/**
 * TODO 封装自定义数据库操作工具类
 */
public class DbOperUtil {

    // db操作实例
    private static MongoDBUtil mongoDBUtil = MongoDBUtil.getMongoDBUtilInstance();

    // 默认操作db:eb_db
    private static String dbName = "eb_db";


    // 根据collectionName获取指定集合
    public static MongoCollection<Document> getCollection(String collectionName) {
        MongoDBUtil mongoDBUtil = MongoDBUtil.getMongoDBUtilInstance();
        // localhost:27017 eb_db
        MongoClient defaultClient = MongoDBUtil.getMongoClientByCredential("127.0.0.1", 27017, "myuser", "eb_db", "000000");
        MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(defaultClient, dbName, collectionName);
        return collection;
    }


    // 保存数据(传入限定json字符串数据)
    public static void insert(String collectionName,String paramJson) {
        MongoCollection<Document> collection = getCollection(collectionName);
        // Map<String,Object> params
        mongoDBUtil.insertDoucument(collection,JSON.parseObject(paramJson,Map.class));
    }

    // 修改数据(传入限定json字符串数据)
    public static void update(String collectionName, String condParamJson,
                                String updateParamJson, boolean MultiUpdate) {
        MongoCollection<Document> collection = getCollection(collectionName);
        // JSON.parseObject(JSONObject.toJSONString(user),Map.class)
        mongoDBUtil.updateDocument(collection, JSON.parseObject(condParamJson,Map.class), JSON.parseObject(updateParamJson,Map.class), MultiUpdate);
    }

    // 根据主键id获取记录
    public static Document getById(String collectionName, String idKey) {
        // 封装查询参数
//        Map<String,Object> conditions = Maps.newHashMap();
//        conditions.put("name","张元");
//        Map<String,Integer> compares = Maps.newHashMap();
//        //        compares.put(MongoConst.GT.getCompareIdentify(),20);
////        compares.put(MongoConst.LTE.getCompareIdentify(),28);
//        String opAnd = MongoConst.AND.getCompareIdentify();
//        Map<String,Object> sortParams = Maps.newHashMap();
//        //        sortParams.put("age",-1);
//
//        FindIterable<Document> documents = mongoDBUtil.queryDocument(getCollection(collectionName),null,opAnd,"age",compares,sortParams,null,1);
//        mongoDBUtil.printDocuments(documents);
        return null;
    }




    // 筛选数据(传入限定json字符串数据)
    public static FindIterable<Document> getByCond(String collectionName, Map<String,Object> conditionParams,
                                                    String op,final String compareField,Map<String,Integer> compares,
                                                    Map<String,Object> sortParams, Integer skip, Integer limit){
        FindIterable<Document> documents = mongoDBUtil.queryDocument(getCollection(collectionName),conditionParams,op,compareField,compares,sortParams,skip,limit);
        mongoDBUtil.printDocuments(documents);
        return documents;
    }








}
