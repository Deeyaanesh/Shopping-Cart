package com.deya.shopping_cart.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {

    private Long productId;
    private String productName;
    private String brand;
    private int quantity;
    private BigDecimal price;
}