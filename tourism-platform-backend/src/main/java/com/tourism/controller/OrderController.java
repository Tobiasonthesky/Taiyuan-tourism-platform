package com.tourism.controller;

import com.tourism.dto.OrderCreateDTO;
import com.tourism.entity.OrderEntity;
import com.tourism.service.OrderService;
import com.tourism.vo.OrderDetailVO;
import com.tourism.vo.OrderItemVO;
import com.tourism.utils.JwtUtil;
import com.tourism.vo.PageVO;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/orders")
@Api(tags = "订单管理")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping
    @ApiOperation("创建订单")
    public Result<OrderEntity> createOrder(@Validated @RequestBody OrderCreateDTO dto, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        OrderEntity order = orderService.createOrder(
                userId,
                dto.getOrderType(),
                dto.getItems(),
                dto.getContactName(),
                dto.getContactPhone(),
                dto.getRemark()
        );
        return Result.success("订单创建成功", order);
    }
    
    @GetMapping
    @ApiOperation("获取订单列表")
    public Result<PageVO<OrderEntity>> getOrders(
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        PageVO<OrderEntity> pageVO = orderService.getOrderList(userId, orderStatus, page, size);
        return Result.success(pageVO);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取订单详情")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable Long id, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        OrderDetailVO detailVO = orderService.getOrderDetail(id, userId);
        return Result.success(detailVO);
    }
    
    @GetMapping("/{id}/items")
    @ApiOperation("获取订单项列表")
    public Result<List<OrderItemVO>> getOrderItems(@PathVariable Long id, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        // 验证订单归属
        orderService.getOrderDetail(id, userId);
        List<OrderItemVO> items = orderService.getOrderItems(id);
        return Result.success(items);
    }
    
    @PutMapping("/{id}/cancel")
    @ApiOperation("取消订单")
    public Result<?> cancelOrder(@PathVariable Long id, HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdFromToken(token);
        orderService.cancelOrder(id, userId);
        return Result.success("订单已取消");
    }
    
    @PostMapping("/{id}/pay")
    @ApiOperation("支付订单")
    public Result<?> payOrder(@PathVariable Long id, @RequestParam String payMethod, HttpServletRequest request) {
        orderService.payOrder(id, payMethod);
        return Result.success("支付成功");
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

