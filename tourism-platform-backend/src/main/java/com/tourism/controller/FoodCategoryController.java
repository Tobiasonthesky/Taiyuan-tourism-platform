package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.FoodCategory;
import com.tourism.mapper.FoodCategoryMapper;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 美食分类控制器
 */
@RestController
@RequestMapping("/foods/categories")
@Api(tags = "美食分类管理")
public class FoodCategoryController {
    
    @Autowired
    private FoodCategoryMapper categoryMapper;
    
    @GetMapping
    @ApiOperation("获取美食分类列表")
    public Result<List<FoodCategory>> getCategories() {
        LambdaQueryWrapper<FoodCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FoodCategory::getStatus, 1);
        wrapper.orderByAsc(FoodCategory::getSortOrder);
        List<FoodCategory> categories = categoryMapper.selectList(wrapper);
        return Result.success(categories);
    }
}

