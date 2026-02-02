package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.ExperienceImage;
import com.tourism.mapper.ExperienceImageMapper;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 体验项目图片控制器
 */
@RestController
@RequestMapping("/experiences/{experienceId}/images")
@Api(tags = "体验项目图片管理")
public class ExperienceImageController {
    
    @Autowired
    private ExperienceImageMapper imageMapper;
    
    @GetMapping
    @ApiOperation("获取体验项目图片列表")
    public Result<List<ExperienceImage>> getImages(@PathVariable Long experienceId) {
        LambdaQueryWrapper<ExperienceImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExperienceImage::getExperienceId, experienceId);
        wrapper.orderByAsc(ExperienceImage::getSortOrder);
        List<ExperienceImage> images = imageMapper.selectList(wrapper);
        return Result.success(images);
    }
}



