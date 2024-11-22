package com.deya.shopping_cart.service.order;

import com.deya.shopping_cart.DTO.OrderDto;
import com.deya.shopping_cart.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertOrderToDto(Order order);
}
