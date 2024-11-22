package com.deya.shopping_cart.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
