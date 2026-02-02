package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.AttractionImage;
import com.tourism.mapper.AttractionImageMapper;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 景点图片控制器
 */
@RestController
@RequestMapping("/attractions/{attractionId}/images")
@Api(tags = "景点图片管理")
public class AttractionImageController {
    
    @Autowired
    private AttractionImageMapper imageMapper;
    
    @GetMapping
    @ApiOperation("获取景点图片列表")
    public Result<List<AttractionImage>> getImages(@PathVariable Long attractionId) {
        LambdaQueryWrapper<AttractionImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttractionImage::getAttractionId, attractionId);
        wrapper.orderByAsc(AttractionImage::getSortOrder);
        List<AttractionImage> images = imageMapper.selectList(wrapper);
        return Result.success(images);
    }
}

