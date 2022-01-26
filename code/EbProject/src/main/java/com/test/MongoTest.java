package com.test;
import com.eb.framework.utils.MongoDBUtil;
import com.google.common.collect.Maps;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoTest {
    public static void main(String[] args) {
        MongoDBUtil mongoDBUtil = MongoDBUtil.getMongoDBUtilInstance();
        // localhost:27017 eb_db
         MongoClient meiyaClient = MongoDBUtil.getMongoClientByCredential("127.0.0.1", 27017, "myuser", "eb_db", "000000");


        try {
             MongoCollection<Document> collection = mongoDBUtil.getMongoCollection(meiyaClient, "eb_db", "user_info");


            Map<String, Object> insert = new HashMap<>();
            //1、测试增加
            insert.put("name","zy");
            insert.put("age","12");
            insert.put("date","2018-04-02T09:44:02.658+0000");
            insert.put("school","厦门理工");
            mongoDBUtil.insertDoucument(collection,insert);
            //2、测试条件、范围、排序查询
           /* Map<String,Object> conditions = Maps.newHashMap();
            conditions.put("name","张元");
            Map<String,Integer> compares = Maps.newHashMap();
            compares.put(MongoConst.GT.getCompareIdentify(),20);
            compares.put(MongoConst.LTE.getCompareIdentify(),28);
            String opAnd = MongoConst.AND.getCompareIdentify();
            Map<String,Object> sortParams = Maps.newHashMap();
            sortParams.put("age",-1);
            FindIterable<Document> documents = mongoDBUtil.queryDocument(collection,null,opAnd,"age",compares,sortParams,null,2);
            mongoDBUtil.printDocuments(documents);*/
            //3、in查询
            /*List<String> names = Lists.newArrayList("张媛","zy","zyy");
            FindIterable<Document> documents = mongoDBUtil.queryDocumentIn(collection,"name",names);
            mongoDBUtil.printDocuments(documents);*/
            //4 批量删除
            /*Map<String,Object> conditionParams = Maps.newHashMap();
            conditionParams.put("school","厦门理工");
            long count = mongoDBUtil.deleteDocument(collection,true,conditionParams);
            System.out.println(count);*/
            //更新
            Map<String, Object> queryParams = Maps.newHashMap();
            queryParams.put("school", "修改了学校");
            Map<String, Object> updateParams = Maps.newHashMap();
            updateParams.put("name", "修改了名字");
            mongoDBUtil.updateDocument(collection, queryParams, updateParams, false);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    }
