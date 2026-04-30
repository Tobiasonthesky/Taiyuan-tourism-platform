package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.StrategyRoute;
import com.tourism.mapper.StrategyRouteMapper;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 攻略路线控制器
 */
@RestController
@RequestMapping("/strategies/{strategyId}/routes")
@Api(tags = "攻略路线管理")
public class StrategyRouteController {
    
    @Autowired
    private StrategyRouteMapper routeMapper;
    
    @GetMapping
    @ApiOperation("获取攻略路线列表")
    public Result<List<StrategyRoute>> getRoutes(@PathVariable Long strategyId) {
        LambdaQueryWrapper<StrategyRoute> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrategyRoute::getStrategyId, strategyId);
        wrapper.orderByAsc(StrategyRoute::getDayNumber);
        wrapper.orderByAsc(StrategyRoute::getRouteOrder);
        List<StrategyRoute> routes = routeMapper.selectList(wrapper);
        return Result.success(routes);
    }
}

