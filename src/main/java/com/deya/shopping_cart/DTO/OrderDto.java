package com.deya.shopping_cart.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderDto {

    private Long orderId;
    private Long userId;
    private LocalDateTime orderDateTime;
    private BigDecimal orderTotalAmount;
    private String status;
    private Set<OrderItemDto> orderItemDto;
}
