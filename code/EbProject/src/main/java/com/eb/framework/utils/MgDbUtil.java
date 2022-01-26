package com.eb.framework.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.*;
import org.bson.types.ObjectId;

interface UpdateCallback {
    DBObject doCallback(DBObject valueDBObject);
}


/**
 * Mongodb操作工具类
 */
@SuppressWarnings("deprecation")
public final class MgDbUtil {

    // 无认证直接访问

    // 设定连接地址
    private static final String HOST = "localhost:27017";

    // 设定数据库名
    private static final String dbName = "eb_db";

    private static Mongo mongo;

    private static DB db;

    static {

        mongo = new Mongo(HOST);
        db = mongo.getDB(dbName);

//        try {
//            mongo = new Mongo(HOST);
//            db = mongo.getDB(dbName);
//            // db.authenticate(username, passwd)
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (MongoException e) {
//            e.printStackTrace();
//        }
    }

    private MgDbUtil() {
    }

    /**
     * 添加操作
     *
     * @param map
     * @param collectionName
     */
    public static void add(Map<String, Object> map, String collectionName) {
        DBObject dbObject = new BasicDBObject(map);
        getCollection(collectionName).insert(dbObject);
    }

    /**
     * 添加操作
     *
     * @param list
     * @param collectionName
     */
    public static void add(List<Map<String, Object>> list, String collectionName) {
        for (Map<String, Object> map : list) {
            add(map, collectionName);
        }
    }

    /**
     * 删除操作
     *
     * @param map
     * @param collectionName
     */
    public static void delete(Map<String, Object> map, String collectionName) {
        DBObject dbObject = new BasicDBObject(map);
        getCollection(collectionName).remove(dbObject);
    }

    /**
     * 删除操作,根据主键
     *
     * @param id
     * @param collectionName
     */
    public static void delete(String id, String collectionName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("_id", new ObjectId(id));
        delete(map, collectionName);
    }

    /**
     * 删除全部
     *
     * @param collectionName
     */
    public static void deleteAll(String collectionName) {
        getCollection(collectionName).drop();
    }

    /**
     * 修改操作
     * 会用一个新文档替换现有文档,文档key结构会发生改变
     * 比如原文档{"_id":"123","name":"zhangsan","age":12}当根据_id修改age
     * value为{"age":12}新建的文档name值会没有,结构发生了改变
     *
     * @param whereMap
     * @param valueMap
     * @param collectionName
     */
    public static void update(Map<String, Object> whereMap, Map<String, Object> valueMap, String collectionName) {
        executeUpdate(collectionName, whereMap, valueMap, new UpdateCallback(){
            @Override
            public DBObject doCallback(DBObject valueDBObject) {
                return valueDBObject;
            }
        });
    }

    /**
     * 修改操作,使用$set修改器
     * 用来指定一个键值,如果键不存在,则自动创建,会更新原来文档, 不会生成新的, 结构不会发生改变
     *
     * @param whereMap
     * @param valueMap
     * @param collectionName
     */
    public static void updateSet(Map<String, Object> whereMap, Map<String, Object> valueMap, String collectionName) {
        executeUpdate(collectionName, whereMap, valueMap, new UpdateCallback(){
            @Override
            public DBObject doCallback(DBObject valueDBObject) {
                return new BasicDBObject("$set", valueDBObject);
            }
        });
    }

    /**
     * 修改操作,使用$inc修改器
     * 修改器键的值必须为数字
     * 如果键存在增加或减少键的值, 如果不存在创建键
     *
     * @param whereMap
     * @param valueMap
     * @param collectionName
     */
    public static void updateInc(Map<String, Object> whereMap, Map<String, Integer> valueMap, String collectionName) {
        executeUpdate(collectionName, whereMap, valueMap, new UpdateCallback(){
            @Override
            public DBObject doCallback(DBObject valueDBObject) {
                return new BasicDBObject("$inc", valueDBObject);
            }
        });
    }

    /**
     * 修改
     *
     * @param collectionName
     * @param whereMap
     * @param valueMap
     * @param updateCallback
     */
    private static void executeUpdate(String collectionName, Map whereMap, Map valueMap, UpdateCallback updateCallback) {
        DBObject whereDBObject = new BasicDBObject(whereMap);
        DBObject valueDBObject = new BasicDBObject(valueMap);
        valueDBObject = updateCallback.doCallback(valueDBObject);
        getCollection(collectionName).update(whereDBObject, valueDBObject);
    }


    /**
     * 获取集合(表)
     *
     * @param collectionName
     * @return
     */
    public static DBCollection getCollection(String collectionName) {
        return db.getCollection(collectionName);
    }


    public static void main(String[] args) {
//        DBCollection collection = getCollection("url");
//        BasicDBObject queryObject = new BasicDBObject("urlMd5","bfa89e563d9509fbc5c6503dd50faf2e");
//        DBObject obj = collection.findOne(queryObject);

        // 根据名称获取指定集合
        DBCollection collection = getCollection("user_info");
//        BasicDBObject queryObject = new BasicDBObject("userName","测试ing");
//        DBObject obj = collection.findOne(queryObject);


        List<Map<String, Object>> list = new ArrayList<>();
        HashMap u1 = new HashMap<>();
        u1.put("userName","hhhh");
        HashMap u2 = new HashMap<>();
        u2.put("userName","xxxx");
        HashMap u3 = new HashMap<>();
        u3.put("userName","wwww");

        list.add(u1);
        list.add(u2);
        list.add(u3);
        add(list,"user_info");

//        System.out.println(obj);

    }
}