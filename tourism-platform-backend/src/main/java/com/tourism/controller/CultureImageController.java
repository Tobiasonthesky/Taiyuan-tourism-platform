package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.CultureImage;
import com.tourism.mapper.CultureImageMapper;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文化图片控制器
 */
@RestController
@RequestMapping("/cultures/{cultureId}/images")
@Api(tags = "文化图片管理")
public class CultureImageController {
    
    @Autowired
    private CultureImageMapper imageMapper;
    
    @GetMapping
    @ApiOperation("获取文化图片列表")
    public Result<List<CultureImage>> getImages(@PathVariable Long cultureId) {
        LambdaQueryWrapper<CultureImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CultureImage::getCultureId, cultureId);
        wrapper.orderByAsc(CultureImage::getSortOrder);
        List<CultureImage> images = imageMapper.selectList(wrapper);
        return Result.success(images);
    }
}



