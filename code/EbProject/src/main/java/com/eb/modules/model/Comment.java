package com.eb.modules.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


@Data
@Document("comment")
public class Comment {

    // ObejctId
//    private String _id;

    // 主键ID
    @Id
    @Field("commentId")
    private String commentId;

    // 评论商品
    @Field("productId")
    private String productId;

    // 评论内容
    @Field("content")
    private String content;

    // 是否匿名
    @Field("isAnonymous")
    private String isAnonymous;

    // 评论人
    @Field("commentor")
    private String commentor;

    // 评论时间
    @Field("commentTime")
    private Date commentTime;

}
