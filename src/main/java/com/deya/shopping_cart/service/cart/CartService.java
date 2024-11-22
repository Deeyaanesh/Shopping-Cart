package com.deya.shopping_cart.service.cart;

import com.deya.shopping_cart.DTO.CartDto;
import com.deya.shopping_cart.DTO.CartItemDto;
import com.deya.shopping_cart.exceptions.ResourceNotFoundException;
import com.deya.shopping_cart.model.Cart;
import com.deya.shopping_cart.model.User;
import com.deya.shopping_cart.repository.CartItemRepository;
import com.deya.shopping_cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    private final ModelMapper modelMapper;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart cartIdInitializer(User user){
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()->{
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public CartDto convertToCarDto(Cart cart) {
        CartDto cartDto = new CartDto();

        cartDto.setId(cart.getId());
        cartDto.setTotalAmount(cart.getTotalAmount());

        Set<CartItemDto> cartItemDtoSet = cart.getItems().stream().map(cartItem -> {
            return new CartItemDto(cartItem.getId(), cartItem.getQuantity(), cartItem.getUnitPrice(), cartItem.getProduct());
        }).collect(Collectors.toSet());

        cartDto.setCartItemsDto(cartItemDtoSet);
        return cartDto;
    }

    @Override
    public CartDto convertCartToDto(Cart cart) {
        return modelMapper.map(cart, CartDto.class);
    }
}
