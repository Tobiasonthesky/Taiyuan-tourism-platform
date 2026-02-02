package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.entity.Announcement;
import com.tourism.mapper.AnnouncementMapper;
import com.tourism.utils.PageUtil;
import com.tourism.vo.PageVO;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动公告控制器
 */
@RestController
@RequestMapping("/announcements")
@Api(tags = "活动公告管理")
public class AnnouncementController {
    
    @Autowired
    private AnnouncementMapper announcementMapper;
    
    @GetMapping
    @ApiOperation("获取公告列表")
    public Result<PageVO<Announcement>> getAnnouncements(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean isBanner,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        
        Page<Announcement> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Announcement::getCategory, category);
        }
        if (isBanner != null) {
            wrapper.eq(Announcement::getIsBanner, isBanner ? 1 : 0);
        }
        wrapper.eq(Announcement::getStatus, 1);
        wrapper.orderByDesc(Announcement::getIsTop);
        wrapper.orderByDesc(Announcement::getSortOrder);
        wrapper.orderByDesc(Announcement::getCreateTime);
        
        Page<Announcement> result = announcementMapper.selectPage(pageParam, wrapper);
        PageVO<Announcement> pageVO = new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
        
        return Result.success(pageVO);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取公告详情")
    public Result<Announcement> getAnnouncementDetail(@PathVariable Long id) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null || announcement.getStatus() == 0) {
            return Result.notFound("公告不存在");
        }
        
        // 增加浏览次数
        announcement.setViewCount(announcement.getViewCount() + 1);
        announcementMapper.updateById(announcement);
        
        return Result.success(announcement);
    }
    
    @GetMapping("/banners")
    @ApiOperation("获取轮播图")
    public Result<List<Announcement>> getBanners() {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getIsBanner, 1);
        wrapper.eq(Announcement::getStatus, 1);
        wrapper.orderByDesc(Announcement::getSortOrder);
        wrapper.last("LIMIT 5");
        List<Announcement> banners = announcementMapper.selectList(wrapper);
        return Result.success(banners);
    }
}

