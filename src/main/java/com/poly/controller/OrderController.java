package com.poly.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.poly.dto.request.OrderRequest;
import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.OrderResponse;
import com.poly.dto.response.PageResponse;
import com.poly.services.OrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/create")
    public ApiResponse<OrderResponse> createOrder(@RequestBody @Valid OrderRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(request))
                .build();
    }

    @PostMapping("/getByUser")
    public ApiResponse<PageResponse<OrderResponse>> getOrderByUser(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .result(orderService.getOrdersByUser(page, size))
                .build();
    }

    @PostMapping("/getByAdmin")
    public ApiResponse<PageResponse<OrderResponse>> getOrderByAdmin(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .result(orderService.getOrdersByAdmin(page, size))
                .build();
    }

    @PostMapping("/getDetail")
    public ApiResponse<OrderResponse> getDetailOrder(@RequestBody @Valid OrderRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getDetailOrder(request))
                .build();
    }
}
