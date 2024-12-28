package com.poly.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.poly.dto.request.CartRequest;
import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.cart.CartResponse;
import com.poly.services.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @PostMapping("/add")
    public ApiResponse<CartResponse> addToCart(@RequestBody @Valid CartRequest request) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.addProductToCart(request))
                .build();
    }

    @GetMapping("/get")
    public ApiResponse<CartResponse> getCart() {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCart())
                .build();
    }

    @GetMapping("/update")
    public ApiResponse<CartResponse> updateCart(@RequestBody @Valid CartRequest request) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.updateCart(request))
                .build();
    }

    @DeleteMapping("/remove")
    public ApiResponse<CartResponse> removeFromCart(@RequestBody @Valid CartRequest request) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.removeProductFromCart(request))
                .build();
    }
}
