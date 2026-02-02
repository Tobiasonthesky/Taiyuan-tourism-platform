package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.AttractionCategory;
import com.tourism.mapper.AttractionCategoryMapper;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 景点分类控制器
 */
@RestController
@RequestMapping("/attractions/categories")
@Api(tags = "景点分类管理")
public class AttractionCategoryController {
    
    @Autowired
    private AttractionCategoryMapper categoryMapper;
    
    @GetMapping
    @ApiOperation("获取景点分类列表")
    public Result<List<AttractionCategory>> getCategories() {
        LambdaQueryWrapper<AttractionCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttractionCategory::getStatus, 1);
        wrapper.orderByAsc(AttractionCategory::getSortOrder);
        List<AttractionCategory> categories = categoryMapper.selectList(wrapper);
        return Result.success(categories);
    }
}

