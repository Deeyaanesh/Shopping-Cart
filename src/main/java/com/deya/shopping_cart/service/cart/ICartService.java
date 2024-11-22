package com.deya.shopping_cart.service.cart;

import com.deya.shopping_cart.DTO.CartDto;
import com.deya.shopping_cart.model.Cart;
import com.deya.shopping_cart.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart cartIdInitializer(User user);

    Cart getCartByUserId(Long userId);

    CartDto convertToCarDto(Cart cart);

    CartDto convertCartToDto(Cart cart);
}
