package com.eb.modules.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * 用户信息实体
 */
@Data
// 文档实体映射
@Document("user_info")
public class User {

    // 数据记录ID（对应Object(_id概念)）
//    private String _id;

    // 主键ID(设定为主键ID)(保留mongodb的_id概念，但使用自定义的id作为业务记录主键)
    @Id
    @Field("userId")
     private String userId;

    // 用户名
    @Field("userName")
    private String userName;

    // 密码
    @Field("password")
    private String password;

    // 邮箱
    @Field("email")
    private String email;

    // 手机号
    @Field("mobile")
    private String mobile;

    // 账号状态
    @Field("userStatus")
    private String userStatus;

    // 备注信息
    @Field("remark")
    private String remark;

    // 用户角色（1-系统管理员；2-普通用户）
    @Field("userRole")
    private String userRole;

    // 记录创建人
    @Field("createBy")
    private String createBy;

    // 记录修改人
    @Field("modifyBy")
    private String modifyBy;

    // 记录创建时间
    @Field("createTime")
    private Date createTime;

    // 记录修改时间
    @Field("modifyTime")
    private Date modifyTime;
}
