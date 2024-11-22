package com.deya.shopping_cart.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(AccessDeniedException.class)
    public static ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e){
        String message = "You don't have permission to access this resource";
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

}
