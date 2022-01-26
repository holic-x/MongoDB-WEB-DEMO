package com.eb.framework.utils;

import com.eb.framework.utils.MongoDBUtil;
import com.mongodb.MongoClient;

public class ConstUtil {


    public static String TB_USER_INFO = "user_info";

    /**
     * 根据设定参数获取操作实例
     * 此处设定默认ip:127.0.0.1\用户：myuser\密码：000000、操作db：eb_db
     * 注意全局变量引用问题
     */
     public static MongoClient DEFAULT_CLIENT = MongoDBUtil.getMongoClientByCredential("127.0.0.1", 27017, "myuser", "eb_db", "000000");

    /** 登录缓存定义 **/
    public static String LOGIN_USER_CACHE = "loginUserCache";

    /** 用户登录账号状态字段定义 **/
    public static String LOGIN_USER_STATUS= "userStatus";

    /** 登录校验字段定义 **/
    public static String LOGIN_RELATE_ROLE_SET = "roleSet";

    /** 登录校验字段定义 **/
    public static String LOGIN_RELATE_AUTH_SET = "authoritySet";

}
