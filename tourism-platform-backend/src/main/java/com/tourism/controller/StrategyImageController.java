package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.StrategyImage;
import com.tourism.mapper.StrategyImageMapper;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ?
 */
@RestController
@RequestMapping("/strategies/{strategyId}/images")
@Api(tags = "")
public class StrategyImageController {
    
    @Autowired
    private StrategyImageMapper imageMapper;
    
    @GetMapping
    @ApiOperation("")
    public Result<List<StrategyImage>> getImages(@PathVariable Long strategyId) {
        LambdaQueryWrapper<StrategyImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrategyImage::getStrategyId, strategyId);
        wrapper.orderByAsc(StrategyImage::getSortOrder);
        List<StrategyImage> images = imageMapper.selectList(wrapper);
        return Result.success(images);
    }
}



