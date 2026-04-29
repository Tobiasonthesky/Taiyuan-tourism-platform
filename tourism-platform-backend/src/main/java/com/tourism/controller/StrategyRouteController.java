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
 * ?
 */
@RestController
@RequestMapping("/strategies/{strategyId}/routes")
@Api(tags = "")
public class StrategyRouteController {
    
    @Autowired
    private StrategyRouteMapper routeMapper;
    
    @GetMapping
    @ApiOperation("")
    public Result<List<StrategyRoute>> getRoutes(@PathVariable Long strategyId) {
        LambdaQueryWrapper<StrategyRoute> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrategyRoute::getStrategyId, strategyId);
        wrapper.orderByAsc(StrategyRoute::getDayNumber);
        wrapper.orderByAsc(StrategyRoute::getRouteOrder);
        List<StrategyRoute> routes = routeMapper.selectList(wrapper);
        return Result.success(routes);
    }
}

