package com.tourism.service;

import com.tourism.dto.StrategyGenerateDTO;
import com.tourism.entity.Strategy;

/**
 * 攻略生成服务接口
 */
public interface StrategyGenerateService {
    
    /**
     * 使用AI生成攻略
     * @param dto 攻略生成请求参数
     * @param userId 用户ID（可选）
     * @return 生成的攻略对象
     */
    Strategy generateStrategy(StrategyGenerateDTO dto, Long userId);
}
