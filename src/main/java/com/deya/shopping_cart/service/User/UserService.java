package com.deya.shopping_cart.service.User;

import com.deya.shopping_cart.DTO.*;
import com.deya.shopping_cart.exceptions.AlreadyExistsException;
import com.deya.shopping_cart.exceptions.ResourceNotFoundException;
import com.deya.shopping_cart.model.Cart;
import com.deya.shopping_cart.model.Image;
import com.deya.shopping_cart.model.User;
import com.deya.shopping_cart.repository.UserRepository;
import com.deya.shopping_cart.request.CreateUserRequest;
import com.deya.shopping_cart.request.UserUpdateRequest;
import com.deya.shopping_cart.service.cart.CartItemService;
import com.deya.shopping_cart.service.cart.CartService;
import com.deya.shopping_cart.service.image.ImageService;
import com.deya.shopping_cart.service.order.OrderService;
import com.deya.shopping_cart.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final OrderService orderService;
    private final ProductService productService;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(rq -> {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException("Oops! " + request.getEmail() + " already exist"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,
                () -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserDto convertUserToDto(User user){
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Cart cart = user.getCart();
        if (cart == null){
            return userDto;
        }
        CartDto cartDto = cartService.convertCartToDto(cart);

        Set<CartItemDto> cartItemDto = user.getCart().getItems()
                .stream()
                .map(cartItem -> {
                    CartItemDto cartItemDto1 = cartItemService.convertCartItemToDto(cartItem);
                    ProductDto productDto = productService.convertProductToDto(cartItem.getProduct());
                    List<ImageDto> imageDtoList = cartItem.getProduct().getImages().stream().map(imageService::convertImageToDto).toList();
                    productDto.setImages(imageDtoList);
                    cartItemDto1.setProductDto(productDto);
                    return cartItemDto1;
                }).collect(Collectors.toSet());

        cartDto.setCartItemsDto(cartItemDto);

        List<OrderDto> orderDto = user.getOrder()
                .stream()
                .map(orderService::convertOrderToDto).toList();

        userDto.setCartDto(cartDto);
        userDto.setOrders(orderDto);
        return userDto;
    }

    @Override
    public User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
