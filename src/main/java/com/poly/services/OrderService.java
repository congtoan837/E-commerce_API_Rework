package com.poly.services;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poly.dto.request.OrderRequest;
import com.poly.dto.response.OrderResponse;
import com.poly.dto.response.PageResponse;
import com.poly.entity.*;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.mapper.OrderMapper;
import com.poly.repositories.CartItemRepository;
import com.poly.repositories.CartRepository;
import com.poly.repositories.OrderRepository;
import com.poly.repositories.VoucherRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    VoucherRepository voucherRepository;

    UserService userService;

    OrderMapper orderMapper;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        User user = userService.getCurrentUser();

        // Lấy giỏ hàng của người dùng
        Cart cart =
                cartRepository.findByUserId(user.getId()).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        Set<OrderItem> orderItems = new HashSet<>();
        long totalAmount = 0;

        Voucher voucher = null;
        for (UUID productId : orderRequest.getProductIds()) {
            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND_IN_CART));

            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .build();

            orderItems.add(orderItem);
            totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }

        long discount = 0;
        if (orderRequest.getVoucherCode() != null
                && !orderRequest.getVoucherCode().isEmpty()) {
            voucher = voucherRepository
                    .findByCode(orderRequest.getVoucherCode())
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

            if (voucher.isActive()
                    && !voucher.isDeleted()
                    && (voucher.getStartDate().isBefore(LocalDate.now())
                            || voucher.getStartDate().isEqual(LocalDate.now()))
                    && (voucher.getEndDate().isAfter(LocalDate.now())
                            || voucher.getEndDate().isEqual(LocalDate.now()))) {

                if (voucher.isPercent()) {
                    discount = totalAmount * (voucher.getValue() / 100);
                } else {
                    discount = voucher.getValue();
                }
            } else {
                throw new AppException(ErrorCode.VOUCHER_INVALID);
            }
        }

        long finalAmount = totalAmount - discount;

        Order order = Order.builder()
                .orderName(orderRequest.getOrderName())
                .orderPhone(orderRequest.getOrderPhone())
                .orderAddress(orderRequest.getOrderAddress())
                .status("PENDING")
                .note(orderRequest.getNote())
                .user(user)
                .orderItems(orderItems)
                .voucher(voucher)
                .originalAmount(totalAmount)
                .discountAmount(discount)
                .totalAmount(finalAmount)
                .build();

        OrderResponse response = orderMapper.toOrderResponse(orderRepository.save(order));

        // Xóa sản phẩm trong giỏ hàng
        cartItemRepository.deleteAll(cart.getCartItems());

        return response;
    }

    public PageResponse<OrderResponse> getOrdersByAdmin(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageResult = orderRepository.findAll(pageable);
        return orderMapper.toPageResponse(pageResult);
    }

    public PageResponse<OrderResponse> getOrdersByUser(int page, int size) {
        User user = userService.getCurrentUser();

        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageResult = orderRepository.findByUserId(user.getId(), pageable);
        return orderMapper.toPageResponse(pageResult);
    }

    public OrderResponse getDetailOrder(OrderRequest request) {
        var result = orderRepository
                .findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        return orderMapper.toOrderResponse(result);
    }
}
