package com.eb.modules.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.eb.framework.shiro.ShiroUtil;
import com.eb.framework.utils.CommonUtil;
import com.eb.modules.dao.CommentDao;
import com.eb.modules.model.Comment;
import com.eb.modules.model.Product;
import com.eb.modules.model.User;
import com.eb.modules.service.CommentService;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public boolean edit(Comment comment) {
        String commentId = comment.getCommentId();
        Timestamp currentDate = CommonUtil.getCurrentTimestamp();
        if (StringUtils.isEmpty(commentId)) {
            // 新增操作
            comment.setCommentId(CommonUtil.getRandomId());
            // 匿名处理
            String isAnonymous = comment.getIsAnonymous();
            if("0".equals(isAnonymous)){
                comment.setCommentor(ShiroUtil.getCurrentUser().getString("userId"));
            }else if("1".equals(isAnonymous)){
                comment.setCommentor("");
            }
            comment.setCommentTime(currentDate);
            commentDao.save(comment);
        } else {
            // 编辑操作
            comment.setCommentTime(currentDate);
            commentDao.save(comment);
        }
        return true;
    }

    @Override
    public boolean delete(List<String> commentIdList) {
        for (String commentId : commentIdList) {
            commentDao.deleteById(commentId);
        }
        return true;
    }

    @Override
    public Map<String, Object> getById(String commentId) {
        Optional<Comment> findComment = commentDao.findById(commentId);
        // 将Comment对象转化为Map集合
        return findComment != null ? JSON.parseObject(JSON.toJSONString(findComment.get()), new TypeReference<Map<String, Object>>() {
        }) : null;
    }

    @Override
    public Page<Comment> getByPage(JSONObject queryCond) {
        Pageable pageable = PageRequest.of(queryCond.getInteger("page") - 1, queryCond.getInteger("limit"));
        return commentDao.getByPage(pageable, queryCond.getString("content"));
    }

    @Override
    public Page<Map<String, Object>> getByPageAndCond(JSONObject queryCond) {
        // 定义封装条件
        int pageSize = queryCond.getInteger("limit");
        int pageNumber = queryCond.getInteger("page");
        String contentParam = queryCond.getString("content");
        String productNameParam = queryCond.getString("productName");


        // 封装筛选条件
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(contentParam)) {
            Pattern pattern = Pattern.compile("^.*" + contentParam + ".*$", Pattern.CASE_INSENSITIVE);
            criteria.and("content").is(pattern); // 模糊查询匹配
        }

        Criteria prodCriteria = new Criteria();
        if (!StringUtils.isEmpty(productNameParam)) {
            Pattern pattern = Pattern.compile("^.*" + productNameParam + ".*$", Pattern.CASE_INSENSITIVE);
            prodCriteria.and("prod.productName").is(pattern); // 模糊查询匹配
        }

        /*数据聚合*/
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                /*关联商品:封装商品名称筛选条件*/
                 Aggregation.lookup("product", "productId", "_id", "prod"),
                Aggregation.match(prodCriteria),// 需注意如果是join的表字段过滤，则筛选条件需在lookupOperation后才会生效
                /*关联评论人*/
                Aggregation.lookup("user_info", "commentor", "_id", "commentor"),
                /*查询起始值*/
                Aggregation.skip(pageNumber > 1 ? (pageNumber - 1) *pageSize : 0),
                /*分页大小*/
                Aggregation.limit(pageSize),
                /*排序*/
                Aggregation.sort(Sort.by(Sort.Order.desc("commentTime"))),
                /*打散prod*/
                Aggregation.unwind("prod")
        );
        List<Map> resMapList = mongoTemplate.aggregate(aggregation, "comment",Map.class).getMappedResults();
//        AggregationResults<BasicDBObject> aggregate = mongoTemplate.aggregate(
//                aggregation, "comment", BasicDBObject.class// A表，是查询的主表
//        );
        // 组装分页对象
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Map<String, Object>> pager = new PageImpl(mongoTemplate.aggregate(
                aggregation, "comment", BasicDBObject.class// A表，是查询的主表
        ).getMappedResults(), pageable, resMapList.size());// 对象转换 将BasicDBObject转换成前面需要的类型.....
        return pager;

        /**
         * 方式2参考（多条件搜索）
         */
        /*
        // 定义查询条件(空指针校验)
        Criteria criteria = new Criteria();
        // 相当于where content = "xxx"
//        criteria.and("content").is(contentParam);
        if (!StringUtils.isEmpty(contentParam)) {
//            Pattern pattern = Pattern.compile("^.*" + contentParam + ".*$", Pattern.CASE_INSENSITIVE);
//            criteria.and("content").is(pattern); // 模糊查询匹配
            criteria.and("content").regex(".*?" + contentParam + ".*", "i");
        }

        // 定义排序条件
        Sort sort = Sort.by(Sort.Direction.DESC, "commentTime");
        // 联合查询总条数，分页用
        Aggregation aggregationCount = Aggregation.newAggregation(Aggregation.match(criteria));//查询条件
        // 联合查询条件（productName = "xxx"）
        Criteria prodCriteria = new Criteria();
        if (!StringUtils.isEmpty(productNameParam)) {
            Pattern pattern = Pattern.compile("^.*" + productNameParam + ".*$", Pattern.CASE_INSENSITIVE);
            prodCriteria.and("productName").is(pattern); // 模糊查询匹配
        }
        Aggregation newAggregation = Aggregation.newAggregation(
                Aggregation.lookup("product", "productId", "_id", "prod"),// 从表名，主表联接字段，从表联接字段，别名（关联查询结果集合名称）
                Aggregation.match(prodCriteria),
                Aggregation.skip(pageSize * (pageNumber - 1L)),//Long类型的参数
                Aggregation.limit(pageSize)
        );

        // 查询 AggregationResults<BasicDBObject>
        AggregationResults<BasicDBObject> aggregate = mongoTemplate.aggregate(
                newAggregation, "comment", BasicDBObject.class// A表，是查询的主表
        );
        int count = mongoTemplate.aggregate(aggregationCount, "comment", BasicDBObject.class).getMappedResults().size();
        // 组装分页对象
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Map<String, Object>> pager = new PageImpl(aggregate.getMappedResults(), pageable, count);
        // 对象转换 将BasicDBObject转换成前面需要的类型.....
        return pager;
        */
    }

    @Override
    public List<Comment> getByCond(JSONObject queryCond) {
        List<Comment> list = commentDao.getByCond(queryCond.getString("productId"), queryCond.getString("commentor"));
        return list;
    }

}
