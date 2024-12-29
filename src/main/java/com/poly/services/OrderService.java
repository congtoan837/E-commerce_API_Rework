package com.poly.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poly.dto.request.OrderRequest;
import com.poly.dto.response.PageResponse;
import com.poly.dto.response.order.OrderResponse;
import com.poly.entity.*;
import com.poly.ex.content.OrderStatus;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.mapper.OrderMapper;
import com.poly.repositories.*;

import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    CartRepository cartRepository;
    VoucherRepository voucherRepository;
    ProductRepository productRepository;
    VariantRepository variantRepository;

    UserService userService;
    TransactionService transactionService;

    OrderMapper orderMapper;

    @Transactional
    public OrderResponse createOrder(@Valid OrderRequest request) {
        User user = userService.getCurrentUser();

        // Lấy giỏ hàng của người dùng
        Cart cart =
                cartRepository.findByUserId(user.getId()).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        Voucher voucher = applyVoucher(request.getVoucherCode());

        List<CartItem> cartItemList = new ArrayList<>(cart.getCartItems());
        cartItemList.sort(Comparator.comparing(item -> item.getVariant() != null
                ? item.getVariant().getPrice()
                : item.getProduct().getPrice()));

        Set<OrderItem> orderItems = new HashSet<>();
        long totalAmount = 0;
        long totalDiscount = 0;
        // Tạo danh sách OrderItem và tính tổng giá trị
        for (CartItem item : cart.getCartItems()) {
            Product productItem = item.getProduct();
            Variant variantItem = item.getVariant();
            int quantity = item.getQuantity();

            long itemPrice;

            if (variantItem != null) { // Xử lý trường hợp có Variant
                Variant variant = variantRepository
                        .findById(variantItem.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.VARIANT_INVALID));
                if (variant.getStockQuantity() < quantity) {
                    throw new AppException(ErrorCode.OUT_OF_STOCK);
                }

                // Cập nhật stockQuantity và soldQuantity của Variant
                variant.setStockQuantity(variant.getStockQuantity() - quantity);
                variant.setSoldQuantity(variant.getSoldQuantity() + quantity);
                variantRepository.save(variant);

                itemPrice = variant.getPrice();
            } else { // Xử lý trường hợp không có Variant
                Product product = productRepository
                        .findById(productItem.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                if (product.getStockQuantity() < quantity) {
                    throw new AppException(ErrorCode.OUT_OF_STOCK);
                }

                product.setStockQuantity(product.getStockQuantity() - quantity);
                product.setSoldQuantity(product.getSoldQuantity() + quantity);
                productRepository.save(product);

                itemPrice = product.getPrice();
            }

            totalAmount += itemPrice * quantity;

            OrderItem orderItem = OrderItem.builder()
                    .product(productItem)
                    .variant(variantItem)
                    .quantity(quantity)
                    .amount(itemPrice * quantity)
                    .discount(0)
                    .build();

            orderItems.add(orderItem);
        }

        if (Objects.nonNull(voucher)) {
            totalDiscount = calculateTotalDiscount(voucher, cartItemList);
        }

        // Phân bổ discount cho từng orderItem
        long distributedDiscount = 0;
        for (OrderItem orderItem : orderItems) {
            long itemDiscount = Math.round((double) orderItem.getAmount() / totalAmount * totalDiscount);
            orderItem.setDiscount(itemDiscount);
            distributedDiscount += itemDiscount;
        }
        // Kiểm tra lại để đảm bảo tổng discount không bị lệch do làm tròn
        long diff = totalDiscount - distributedDiscount;
        if (diff != 0) {
            for (OrderItem orderItem : orderItems) {
                if (diff > 0) {
                    orderItem.setDiscount(orderItem.getDiscount() + 1);
                    diff--;
                } else {
                    orderItem.setDiscount(orderItem.getDiscount() - 1);
                    diff++;
                }
                if (diff == 0) break;
            }
        }
        long finalAmount = totalAmount - totalDiscount;

        // Tạo đơn hàng
        Order orderBuilder = Order.builder()
                .status("PENDING")
                .note(request.getNote())
                .user(user)
                .orderItems(orderItems)
                .voucher(voucher)
                .originalAmount(totalAmount)
                .discountAmount(totalDiscount)
                .totalAmount(finalAmount)
                .build();

        // Tạo yêu cầu thanh toán
        var paymentUrl = transactionService.initiateTransaction(orderBuilder);
        orderBuilder.setPaymentMethod(request.getPaymentMethod());
        orderBuilder.setPaymentUrl(paymentUrl);

        Order order = orderRepository.save(orderBuilder);

        // Xóa sản phẩm trong giỏ hàng
        cartRepository.delete(cart);

        return orderMapper.toOrderResponse(order);
    }

    private Voucher applyVoucher(String voucherCode) {
        if (StringUtils.isBlank(voucherCode)) {
            return null;
        }

        return voucherRepository
                .findByCodeAndIsDeletedFalse(voucherCode)
                .filter(this::isVoucherValid)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_INVALID));
    }

    // Kiểm tra voucher có hợp lệ không
    private boolean isVoucherValid(Voucher voucher) {
        return "active".equals(voucher.getStatus())
                && voucher.getConsumedQuantity() < voucher.getLimitQuantity()
                && voucher.getStartDate().isAfter(LocalDate.now())
                && voucher.getEndDate().isBefore(LocalDate.now());
    }

    // Tính toán giá trị giảm giá
    private long calculateTotalDiscount(Voucher voucher, List<CartItem> cartItems) {
        long totalDiscount = 0;
        long totalCartValue = cartItems.stream()
                .mapToLong(item -> (item.getVariant() != null
                                ? item.getVariant().getPrice()
                                : item.getProduct().getPrice())
                        * item.getQuantity())
                .sum();
        long maxDiscountAmount = voucher.getValue();

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            long price = item.getVariant() != null
                    ? item.getVariant().getPrice()
                    : item.getProduct().getPrice();
            if (isVoucherApplicableToProduct(voucher, product)) {
                long itemValue = price * item.getQuantity();
                long itemDiscount;

                if (voucher.isPercent()) {
                    itemDiscount = itemValue * voucher.getValue() / 100;
                } else {
                    itemDiscount = voucher.getValue();
                }
                if (totalDiscount + itemDiscount > maxDiscountAmount) {
                    itemDiscount = maxDiscountAmount - totalDiscount;
                }
                totalDiscount += itemDiscount;
            }
            if (totalDiscount >= maxDiscountAmount) break;
        }
        return totalDiscount;
    }

    // Kiểm tra voucher có áp dụng cho sản phẩm này hay không
    private boolean isVoucherApplicableToProduct(Voucher voucher, Product product) {
        // Kiểm tra nếu voucher áp dụng cho sản phẩm
        if (voucher.getProducts() != null && voucher.getProducts().contains(product)) {
            return true;
        }

        // Kiểm tra nếu voucher áp dụng cho loại hàng hóa mà sản phẩm thuộc về
        if (voucher.getCategories() != null) {
            for (Category category : product.getCategories()) {
                if (voucher.getCategories().contains(category)) {
                    return true;
                }
            }
        }

        // Nếu không có điều kiện nào thỏa mãn, voucher không áp dụng
        return false;
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
        var order = orderRepository
                .findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        return orderMapper.toOrderResponse(order);
    }

    public OrderResponse cancelOrder(OrderRequest request) {
        Order order = orderRepository
                .findById(request.getId())
                .filter(o -> OrderStatus.PENDING.getName().equals(o.getStatus()))
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Cập nhật trạng thái đơn hàng
        order.setStatus(OrderStatus.CANCELLED.getName());

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    public void cancelUnpaidOrders() {
        List<Order> unpaidOrders = orderRepository.findByStatus(OrderStatus.PENDING.getName());
        unpaidOrders.stream()
                .filter(order ->
                        order.getCreateTime().isBefore(LocalDateTime.now().minusHours(1)))
                .forEach(order -> {
                    order.setStatus(OrderStatus.CANCELLED.getName());
                    orderRepository.save(order);
                });
    }
}
