package com.tourism.vo;

import lombok.Data;

import java.util.List;

/**
 * 分页视图对象
 */
@Data
public class PageVO<T> {
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 总页数
     */
    private Long pages;
    
    /**
     * 当前页码
     */
    private Long current;
    
    /**
     * 每页数量
     */
    private Long size;
    
    /**
     * 数据列表
     */
    private List<T> records;
    
    public PageVO() {
    }
    
    public PageVO(Long total, Long pages, Long current, Long size, List<T> records) {
        this.total = total;
        this.pages = pages;
        this.current = current;
        this.size = size;
        this.records = records;
    }
}

