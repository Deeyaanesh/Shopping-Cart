package com.deya.shopping_cart.data;

import com.deya.shopping_cart.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String role);
}
