package com.deya.shopping_cart.service.order;

import com.deya.shopping_cart.DTO.OrderItemDto;
import com.deya.shopping_cart.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService implements IOrderItemService{

    private final ModelMapper modelMapper;

    @Override
    public OrderItemDto convertItemToDto(OrderItem orderItem) {
        return modelMapper.map(orderItem, OrderItemDto.class);
    }
}
