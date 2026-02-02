package com.tourism.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 攻略生成请求DTO
 */
@Data
public class StrategyGenerateDTO {
    
    /**
     * 游玩天数
     */
    @NotNull(message = "游玩天数不能为空")
    @Min(value = 1, message = "游玩天数至少为1天")
    private Integer duration;
    
    /**
     * 预算（元，可选）
     */
    private BigDecimal budget;
    
    /**
     * 主题：亲子、情侣、摄影、文化、美食等
     */
    private String theme;
    
    /**
     * 兴趣偏好（多个用逗号分隔）
     */
    private String interests;
    
    /**
     * 特殊要求（自由文本）
     */
    private String requirements;
    
    /**
     * 最佳季节
     */
    private String bestSeason;
}
