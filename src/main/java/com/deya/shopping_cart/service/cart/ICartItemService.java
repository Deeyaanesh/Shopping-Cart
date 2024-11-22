package com.deya.shopping_cart.service.cart;

import com.deya.shopping_cart.model.CartItem;
import com.deya.shopping_cart.model.Product;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
    CartItem getCartItem(Long cartId, Long productId);
}
