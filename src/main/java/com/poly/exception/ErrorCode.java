package com.poly.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(-9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(-1, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(-1, "Access denied", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(-1, "Invalid token signature", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(-1, "Refresh token has expired", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(-1, "user not found", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS(-1, "Username exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(-1, "Username invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(-1, "Password invalid", HttpStatus.BAD_REQUEST),
    USERNAME_SHORT(-1, "Username must be at least 6 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_SHORT(-1, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    ROLES_INVALID(-1, "Roles invalid", HttpStatus.BAD_REQUEST),
    INPUT_VALID(-1, "Input invalid", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(-1, "Email invalid", HttpStatus.BAD_REQUEST),
    NOT_FOUND(-1, "Not found", HttpStatus.BAD_REQUEST),
    CART_NOT_FOUND(-1, "Cart not found", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND_IN_CART(-1, "product not found in cart", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_FOUND(-1, "Voucher not found", HttpStatus.BAD_REQUEST),
    VOUCHER_INVALID(-1, "Voucher invalid", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(-1, "Order not found", HttpStatus.BAD_REQUEST);
    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
