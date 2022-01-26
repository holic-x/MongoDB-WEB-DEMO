package com.eb.framework.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import javax.validation.constraints.Min;

@NoArgsConstructor
@AllArgsConstructor
public class SpringDataPageAble implements Pageable {

    @Min(1)
    private Integer pageNumber = 1;
    @Min(1)
    private Integer pageSize = 10;
    private Sort sort;

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    // 当前页面
    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    // 每一页显示的条数

    @Override
    public int getPageSize() {
        return getPagesize();
    }

    // 第二页所需要增加的数量

    @Override
    public long getOffset() {
        return (getPageNumber() - 1) * getPagesize();
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    public void setPagenumber(Integer pagenumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPagesize() {
        return this.pageSize;
    }

    public void setPagesize(Integer pagesize) {
        this.pageSize = pagesize;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}