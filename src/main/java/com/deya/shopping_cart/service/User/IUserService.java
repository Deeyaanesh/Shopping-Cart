package com.deya.shopping_cart.service.User;

import com.deya.shopping_cart.DTO.UserDto;
import com.deya.shopping_cart.model.User;
import com.deya.shopping_cart.request.CreateUserRequest;
import com.deya.shopping_cart.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
