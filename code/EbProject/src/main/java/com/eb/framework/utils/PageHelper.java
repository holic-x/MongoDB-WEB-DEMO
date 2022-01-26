package com.eb.framework.utils;

import com.eb.modules.model.User;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageHelper<T> {



    /**
     * 当前页数
     */
    private long currPage;
    /**
     * 每页记录数
     */
    private long pageSize;
    /**
     * 总页数
     */
    private long totalPage;
    /**
     * 总记录数
     */
    private long totalCount;
    /**
     * 列表数据
     */
    private List<T> list;

    public PageHelper(long pageNum, long pageSize,long totalPage, long totalCount,  List<T> list) {
        this.currPage = pageNum;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
        this.list = list;
    }

    public PageHelper(long pageNum, long pageSize, List<T> list) {
        this.currPage = pageNum;
        this.pageSize = pageSize;
        this.list = list;
    }

    // 通过经由mongodb分页后的信息封装数据
    public PageHelper(Page<T> pageData) {
        this.currPage = pageData.getNumber();
        this.pageSize = pageData.getSize();
        this.totalPage = pageData.getTotalPages();
        this.totalCount = pageData.getTotalElements();
        this.list = pageData.getContent();
    }
}