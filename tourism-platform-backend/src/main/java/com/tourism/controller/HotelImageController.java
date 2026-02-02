package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.HotelImage;
import com.tourism.mapper.HotelImageMapper;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 酒店图片控制器
 */
@RestController
@RequestMapping("/hotels/{hotelId}/images")
@Api(tags = "酒店图片管理")
public class HotelImageController {
    
    @Autowired
    private HotelImageMapper imageMapper;
    
    @GetMapping
    @ApiOperation("获取酒店图片列表")
    public Result<List<HotelImage>> getImages(@PathVariable Long hotelId) {
        LambdaQueryWrapper<HotelImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HotelImage::getHotelId, hotelId);
        wrapper.orderByAsc(HotelImage::getSortOrder);
        List<HotelImage> images = imageMapper.selectList(wrapper);
        return Result.success(images);
    }
}



