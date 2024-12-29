package com.poly.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(-9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    // Xác thực và bảo mật
    UNAUTHENTICATED(1001, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1002, "Access denied", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(1003, "Invalid token signature", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1004, "Refresh token has expired", HttpStatus.BAD_REQUEST),

    // Người dùng
    USER_NOT_FOUND(2001, "User not found", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS(2002, "Username exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(2003, "Username invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(2004, "Password invalid", HttpStatus.BAD_REQUEST),
    USERNAME_SHORT(2005, "Username must be at least 6 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_SHORT(2006, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    ROLES_INVALID(2007, "Roles invalid", HttpStatus.BAD_REQUEST),
    INPUT_VALID(2008, "Input invalid", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(2009, "Email invalid", HttpStatus.BAD_REQUEST),

    // Sản phẩm
    PRODUCT_NOT_FOUND(3001, "Product not found", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(3002, "Category not found", HttpStatus.BAD_REQUEST),
    VARIANT_INVALID(3003, "Variant invalid", HttpStatus.BAD_REQUEST),
    OUT_OF_STOCK(3009, "Out of stock", HttpStatus.BAD_REQUEST),

    // Giỏ hàng và đơn hàng
    CART_NOT_FOUND(4001, "Cart not found", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND_IN_CART(4002, "Product not found in cart", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_FOUND(4003, "Voucher not found", HttpStatus.BAD_REQUEST),
    VOUCHER_INVALID(4004, "Voucher invalid", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(4005, "Order not found", HttpStatus.BAD_REQUEST),
    QUANTITY_INVALID(4006, "Quantity invalid", HttpStatus.BAD_REQUEST),

    ;
    private final int code; // Mã lỗi
    private final String message; // Thông báo lỗi
    private final HttpStatus statusCode; // HTTP Status cho phản hồi
}
