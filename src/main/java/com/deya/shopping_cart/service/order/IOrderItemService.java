package com.deya.shopping_cart.service.order;

import com.deya.shopping_cart.DTO.OrderItemDto;
import com.deya.shopping_cart.model.OrderItem;

import java.util.Set;

public interface IOrderItemService {
    OrderItemDto convertItemToDto(OrderItem orderItem);
}
