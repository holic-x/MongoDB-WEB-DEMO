package com.eb.modules.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


@Data
@Document("product")
public class Product {

    // ObejctId
    // @Field("_id")
     // private String _id;

    // 主键ID
    @Id
    // @Indexed(unique = true)
    @Field("productId") // 重定义_id映射为主键id（此处mongodb执行生成文档不会自动再生成_id字段）
    private String productId;

    // 商品编号
    @Field("productNum")
    private String productNum;

    // 商品图片url
    @Field("picUrl")
    private String picUrl;

    // 商品分类
    @Field("classify")
    private String classify;

    // 商品名称
    @Field("productName")
    private String productName;

    // 库存
    @Field("stock")
    private String stock;

    // 供货商
    @Field("vendor")
    private String vendor;

    // 备注信息
    @Field("remark")
    private String remark;

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
