package com.tourism.dto;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 分页DTO
 */
@Data
public class PageDTO {
    
    /**
     * 当前页码，默认1
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer page = 1;
    
    /**
     * 每页数量，默认10
     */
    @Min(value = 1, message = "每页数量必须大于0")
    private Integer size = 10;
    
    /**
     * 获取当前页码（MyBatis Plus使用）
     */
    public Integer getCurrent() {
        return page;
    }
}

