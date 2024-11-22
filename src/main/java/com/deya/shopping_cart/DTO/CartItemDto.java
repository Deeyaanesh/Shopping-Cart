package com.deya.shopping_cart.DTO;

import com.deya.shopping_cart.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDto {
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private ProductDto productDto;

    public CartItemDto(Long id, int quantity, BigDecimal unitPrice, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.productDto = new ProductDto(product.getId(), product.getName(), product.getBrand(), product.getPrice(), product.getQuantity(), product.getDescription(), product.getCategory(), product.getImages());
    }
}