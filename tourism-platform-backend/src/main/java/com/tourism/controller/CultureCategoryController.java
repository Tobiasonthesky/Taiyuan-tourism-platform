package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.CultureCategory;
import com.tourism.mapper.CultureCategoryMapper;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文化分类控制器
 */
@RestController
@RequestMapping("/cultures/categories")
@Api(tags = "文化分类管理")
public class CultureCategoryController {
    
    @Autowired
    private CultureCategoryMapper categoryMapper;
    
    @GetMapping
    @ApiOperation("获取文化分类列表")
    public Result<List<CultureCategory>> getCategories() {
        LambdaQueryWrapper<CultureCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CultureCategory::getStatus, 1);
        wrapper.orderByAsc(CultureCategory::getSortOrder);
        List<CultureCategory> categories = categoryMapper.selectList(wrapper);
        return Result.success(categories);
    }
}

