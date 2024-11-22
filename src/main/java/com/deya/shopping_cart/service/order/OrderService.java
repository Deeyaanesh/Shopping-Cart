package com.deya.shopping_cart.service.order;

import com.deya.shopping_cart.DTO.OrderDto;
import com.deya.shopping_cart.DTO.OrderItemDto;
import com.deya.shopping_cart.enums.OrderStatus;
import com.deya.shopping_cart.exceptions.ResourceNotFoundException;
import com.deya.shopping_cart.model.Cart;
import com.deya.shopping_cart.model.Order;
import com.deya.shopping_cart.model.OrderItem;
import com.deya.shopping_cart.model.Product;
import com.deya.shopping_cart.repository.OrderRepository;
import com.deya.shopping_cart.repository.ProductRepository;
import com.deya.shopping_cart.service.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService  implements IOrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final OrderItemService orderItemService;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems =  createOrderItems(order, cart);

        order.setOrderItems(new HashSet<>(orderItems));
        order.setOrderTotalAmount(calculateTotalAmount(orderItems));

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems){
        return orderItems
                .stream()
                .map(orderItem ->
                        orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository
                .findById(orderId)
                .map(this::convertOrderToDto).orElseThrow(() -> new ResourceNotFoundException("Order Not found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertOrderToDto).toList();
    }

    @Override
    public OrderDto convertOrderToDto(Order order){
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);

        Set<OrderItem> orderItem = order.getOrderItems();
        Set<OrderItemDto> orderItemDtoSet = orderItem.stream()
                .map(orderItem1 -> {
                    Product product = orderItem1.getProduct();
                    OrderItemDto orderItemDto = orderItemService.convertItemToDto(orderItem1);
                    orderItemDto.setProductId(product.getId());
                    orderItemDto.setBrand(product.getBrand());
                    return orderItemDto;
                })
                .collect(Collectors.toSet());
        orderDto.setOrderItemDto(orderItemDtoSet);

        return orderDto;
    }

}
