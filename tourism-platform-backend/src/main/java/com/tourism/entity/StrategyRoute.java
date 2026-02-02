package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 攻略路线实体类
 */
@Data
@TableName("strategy_route")
public class StrategyRoute {
    
    /**
     * 路线ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 攻略ID
     */
    private Long strategyId;
    
    /**
     * 第几天
     */
    private Integer dayNumber;
    
    /**
     * 路线顺序
     */
    private Integer routeOrder;
    
    /**
     * 景点ID
     */
    private Long attractionId;
    
    /**
     * 美食ID
     */
    private Long foodId;
    
    /**
     * 地点名称
     */
    private String name;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 开始时间
     */
    private String startTime;
    
    /**
     * 结束时间
     */
    private String endTime;
    
    /**
     * 费用
     */
    private BigDecimal cost;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}

