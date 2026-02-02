package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tourism.dto.OrderItemDTO;
import com.tourism.entity.OrderEntity;
import com.tourism.entity.OrderItem;
import com.tourism.exception.BusinessException;
import com.tourism.entity.Attraction;
import com.tourism.entity.Hotel;
import com.tourism.entity.HotelRoom;
import com.tourism.entity.Experience;
import com.tourism.mapper.AttractionMapper;
import com.tourism.mapper.HotelMapper;
import com.tourism.mapper.HotelRoomMapper;
import com.tourism.mapper.ExperienceMapper;
import com.tourism.mapper.OrderItemMapper;
import com.tourism.mapper.OrderMapper;
import com.tourism.service.OrderService;
import com.tourism.utils.OrderNoUtil;
import com.tourism.utils.PageUtil;
import com.tourism.vo.OrderDetailVO;
import com.tourism.vo.OrderItemVO;
import com.tourism.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private AttractionMapper attractionMapper;
    
    @Autowired
    private HotelMapper hotelMapper;
    
    @Autowired
    private HotelRoomMapper hotelRoomMapper;
    
    @Autowired
    private ExperienceMapper experienceMapper;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderEntity createOrder(Long userId, String orderType, List<OrderItemDTO> items,
                                   String contactName, String contactPhone, String remark) {
        // 创建订单
        OrderEntity order = new OrderEntity();
        order.setOrderNo(OrderNoUtil.generateOrderNo());
        order.setUserId(userId);
        order.setOrderType(orderType);
        order.setContactName(contactName);
        order.setContactPhone(contactPhone);
        order.setRemark(remark);
        order.setOrderStatus(0); // 待支付
        order.setPayStatus(0); // 未支付
        
        // 计算订单金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemDTO itemDTO : items) {
            BigDecimal unitPrice = getItemPrice(itemDTO.getItemType(), itemDTO.getItemId());
            // 如果是酒店订单，使用房间价格
            if ("hotel".equals(itemDTO.getItemType()) && itemDTO.getRoomPrice() != null) {
                unitPrice = itemDTO.getRoomPrice();
            }
            totalAmount = totalAmount.add(unitPrice.multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
        }
        
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPayAmount(totalAmount);
        
        orderMapper.insert(order);
        
        // 创建订单项
        for (OrderItemDTO itemDTO : items) {
            BigDecimal unitPrice = getItemPrice(itemDTO.getItemType(), itemDTO.getItemId());
            String itemName = getItemName(itemDTO.getItemType(), itemDTO.getItemId());
            String itemImage = getItemImage(itemDTO.getItemType(), itemDTO.getItemId());
            
            // 如果是酒店订单，使用房间价格
            if ("hotel".equals(itemDTO.getItemType()) && itemDTO.getRoomPrice() != null) {
                unitPrice = itemDTO.getRoomPrice();
            }
            
            // 检查库存（仅酒店房间）
            if ("hotel".equals(itemDTO.getItemType())) {
                HotelRoom room = hotelRoomMapper.selectById(itemDTO.getItemId());
                if (room != null && room.getStock() != null) {
                    if (room.getStock() < itemDTO.getQuantity()) {
                        throw new BusinessException("房间库存不足，当前库存：" + room.getStock());
                    }
                }
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setItemType(itemDTO.getItemType());
            orderItem.setItemId(itemDTO.getItemId());
            orderItem.setItemName(itemName);
            orderItem.setItemImage(itemImage);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(unitPrice);
            orderItem.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            
            // 处理使用日期
            if (itemDTO.getUseDate() != null) {
                if (itemDTO.getUseDate() instanceof Date) {
                    orderItem.setUseDate((Date) itemDTO.getUseDate());
                } else if (itemDTO.getUseDate() instanceof String) {
                    try {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                        orderItem.setUseDate(sdf.parse((String) itemDTO.getUseDate()));
                    } catch (Exception e) {
                        // 解析失败，忽略
                    }
                }
            }
            orderItem.setUseTime(itemDTO.getUseTime());
            
            // 如果是酒店订单，保存房间信息到 extraInfo
            if ("hotel".equals(itemDTO.getItemType())) {
                Map<String, Object> extraInfo = new HashMap<>();
                if (itemDTO.getRoomType() != null) {
                    extraInfo.put("roomType", itemDTO.getRoomType());
                }
                if (itemDTO.getRoomName() != null) {
                    extraInfo.put("roomName", itemDTO.getRoomName());
                }
                if (itemDTO.getRoomPrice() != null) {
                    extraInfo.put("roomPrice", itemDTO.getRoomPrice());
                }
                if (itemDTO.getCheckInDate() != null) {
                    extraInfo.put("checkInDate", itemDTO.getCheckInDate());
                }
                if (itemDTO.getCheckOutDate() != null) {
                    extraInfo.put("checkOutDate", itemDTO.getCheckOutDate());
                }
                if (!extraInfo.isEmpty()) {
                    try {
                        orderItem.setExtraInfo(objectMapper.writeValueAsString(extraInfo));
                    } catch (Exception e) {
                        // JSON序列化失败，忽略
                    }
                }
            }
            
            orderItemMapper.insert(orderItem);
        }
        
        return order;
    }
    
    @Override
    public PageVO<OrderEntity> getOrderList(Long userId, Integer orderStatus, Integer page, Integer size) {
        Page<OrderEntity> pageParam = PageUtil.createPage(page, size);
        LambdaQueryWrapper<OrderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderEntity::getUserId, userId);
        if (orderStatus != null) {
            wrapper.eq(OrderEntity::getOrderStatus, orderStatus);
        }
        wrapper.orderByDesc(OrderEntity::getCreateTime);
        
        Page<OrderEntity> result = orderMapper.selectPage(pageParam, wrapper);
        return new PageVO<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                result.getRecords()
        );
    }
    
    @Override
    public OrderDetailVO getOrderDetail(Long orderId, Long userId) {
        OrderEntity order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权查看此订单");
        }
        
        OrderDetailVO detailVO = new OrderDetailVO();
        detailVO.setOrder(order);
        detailVO.setItems(getOrderItems(orderId));
        
        return detailVO;
    }
    
    @Override
    public List<OrderItemVO> getOrderItems(Long orderId) {
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        wrapper.orderByAsc(OrderItem::getId);
        List<OrderItem> items = orderItemMapper.selectList(wrapper);
        
        // 转换为OrderItemVO，解析房间信息
        return items.stream()
                .map(this::convertToItemVO)
                .collect(Collectors.toList());
    }
    
    /**
     * 将OrderItem转换为OrderItemVO，解析房间信息
     */
    private OrderItemVO convertToItemVO(OrderItem item) {
        OrderItemVO vo = new OrderItemVO();
        vo.setId(item.getId());
        vo.setOrderId(item.getOrderId());
        vo.setItemType(item.getItemType());
        vo.setItemId(item.getItemId());
        vo.setItemName(item.getItemName());
        vo.setItemImage(item.getItemImage());
        vo.setQuantity(item.getQuantity());
        vo.setUnitPrice(item.getUnitPrice());
        vo.setTotalPrice(item.getTotalPrice());
        vo.setUseDate(item.getUseDate());
        vo.setUseTime(item.getUseTime());
        
        // 解析extraInfo中的房间信息
        if (item.getExtraInfo() != null && !item.getExtraInfo().isEmpty()) {
            try {
                Map<String, Object> extraInfo = objectMapper.readValue(item.getExtraInfo(), 
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
                vo.setExtraInfo(extraInfo);
                
                // 提取房间相关字段
                if (extraInfo.containsKey("roomType")) {
                    vo.setRoomType((String) extraInfo.get("roomType"));
                }
                if (extraInfo.containsKey("roomName")) {
                    vo.setRoomName((String) extraInfo.get("roomName"));
                }
                if (extraInfo.containsKey("roomPrice")) {
                    Object priceObj = extraInfo.get("roomPrice");
                    if (priceObj instanceof Number) {
                        vo.setRoomPrice(new BigDecimal(priceObj.toString()));
                    }
                }
                if (extraInfo.containsKey("checkInDate")) {
                    vo.setCheckInDate((String) extraInfo.get("checkInDate"));
                }
                if (extraInfo.containsKey("checkOutDate")) {
                    vo.setCheckOutDate((String) extraInfo.get("checkOutDate"));
                }
            } catch (Exception e) {
                // 解析失败，忽略
            }
        }
        
        return vo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, Long userId) {
        OrderEntity order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权取消此订单");
        }
        // 允许取消待支付和已支付的订单（已使用、已取消、已退款的订单不能取消）
        if (order.getOrderStatus() == 2 || order.getOrderStatus() == 3 || order.getOrderStatus() == 4) {
            throw new BusinessException("订单状态不允许取消");
        }
        
        // 如果订单已支付，需要恢复库存
        if (order.getPayStatus() == 1 && order.getOrderStatus() == 1) {
            restoreStock(orderId);
            // 已支付的订单取消后，订单状态改为已退款
            order.setOrderStatus(4); // 已退款
        } else {
            // 待支付的订单取消
            order.setOrderStatus(3); // 已取消
        }
        
        order.setCancelTime(new Date());
        orderMapper.updateById(order);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long orderId, String payMethod) {
        OrderEntity order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getOrderStatus() != 0) {
            throw new BusinessException("订单状态不允许支付");
        }
        
        // 扣减库存（仅对酒店房间）
        decreaseStock(orderId);
        
        order.setOrderStatus(1); // 已支付
        order.setPayStatus(1); // 已支付
        order.setPayMethod(payMethod);
        order.setPayTime(new Date());
        orderMapper.updateById(order);
    }
    
    /**
     * 扣减库存（支付订单时）
     */
    private void decreaseStock(Long orderId) {
        List<OrderItem> items = getOrderItemsInternal(orderId);
        for (OrderItem item : items) {
            if ("hotel".equals(item.getItemType())) {
                HotelRoom room = hotelRoomMapper.selectById(item.getItemId());
                if (room != null && room.getStock() != null) {
                    int newStock = room.getStock() - item.getQuantity();
                    if (newStock < 0) {
                        throw new BusinessException("房间库存不足");
                    }
                    room.setStock(newStock);
                    hotelRoomMapper.updateById(room);
                }
            }
        }
    }
    
    /**
     * 恢复库存（取消订单时）
     */
    private void restoreStock(Long orderId) {
        List<OrderItem> items = getOrderItemsInternal(orderId);
        for (OrderItem item : items) {
            if ("hotel".equals(item.getItemType())) {
                HotelRoom room = hotelRoomMapper.selectById(item.getItemId());
                if (room != null && room.getStock() != null) {
                    room.setStock(room.getStock() + item.getQuantity());
                    hotelRoomMapper.updateById(room);
                }
            }
        }
    }
    
    /**
     * 获取订单项列表（内部方法，不返回VO）
     */
    private List<OrderItem> getOrderItemsInternal(Long orderId) {
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        wrapper.orderByAsc(OrderItem::getId);
        return orderItemMapper.selectList(wrapper);
    }
    
    /**
     * 获取项目价格
     */
    private BigDecimal getItemPrice(String itemType, Long itemId) {
        switch (itemType) {
            case "ticket":
                Attraction attraction = attractionMapper.selectById(itemId);
                if (attraction != null && attraction.getTicketPrice() != null) {
                    return attraction.getTicketPrice();
                }
                break;
            case "hotel":
                HotelRoom room = hotelRoomMapper.selectById(itemId);
                if (room != null && room.getPrice() != null) {
                    return room.getPrice();
                }
                break;
            case "experience":
                Experience experience = experienceMapper.selectById(itemId);
                if (experience != null && experience.getPrice() != null) {
                    return experience.getPrice();
                }
                break;
            default:
                throw new BusinessException("不支持的项目类型");
        }
        throw new BusinessException("项目不存在或价格未设置");
    }
    
    /**
     * 获取项目名称
     */
    private String getItemName(String itemType, Long itemId) {
        switch (itemType) {
            case "ticket":
                Attraction attraction = attractionMapper.selectById(itemId);
                if (attraction != null) {
                    return attraction.getName() + "门票";
                }
                break;
            case "hotel":
                HotelRoom room = hotelRoomMapper.selectById(itemId);
                if (room != null) {
                    Hotel hotel = hotelMapper.selectById(room.getHotelId());
                    if (hotel != null) {
                        return hotel.getName() + "-" + room.getRoomName();
                    }
                }
                break;
            case "experience":
                Experience experience = experienceMapper.selectById(itemId);
                if (experience != null) {
                    return experience.getName();
                }
                break;
            default:
                throw new BusinessException("不支持的项目类型");
        }
        throw new BusinessException("项目不存在");
    }
    
    /**
     * 获取项目图片
     */
    private String getItemImage(String itemType, Long itemId) {
        switch (itemType) {
            case "ticket":
                Attraction attraction = attractionMapper.selectById(itemId);
                if (attraction != null) {
                    return attraction.getCoverImage();
                }
                break;
            case "hotel":
                HotelRoom room = hotelRoomMapper.selectById(itemId);
                if (room != null) {
                    return room.getImage();
                }
                break;
            case "experience":
                Experience experience = experienceMapper.selectById(itemId);
                if (experience != null) {
                    return experience.getCoverImage();
                }
                break;
            default:
                return "";
        }
        return "";
    }
}

