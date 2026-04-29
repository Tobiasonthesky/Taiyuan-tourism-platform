package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.FoodImage;
import com.tourism.mapper.FoodImageMapper;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/foods/{foodId}/images")
@Api(tags = "")
public class FoodImageController {
    
    @Autowired
    private FoodImageMapper imageMapper;
    
    @GetMapping
    @ApiOperation("")
    public Result<List<FoodImage>> getImages(@PathVariable Long foodId) {
        LambdaQueryWrapper<FoodImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FoodImage::getFoodId, foodId);
        wrapper.orderByAsc(FoodImage::getSortOrder);
        List<FoodImage> images = imageMapper.selectList(wrapper);
        return Result.success(images);
    }
}

