package com.poly.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.poly.dto.request.CartRequest;
import com.poly.dto.response.cart.CartResponse;
import com.poly.entity.*;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.mapper.CartMapper;
import com.poly.repositories.CartRepository;
import com.poly.repositories.ProductRepository;
import com.poly.repositories.VariantRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    VariantRepository variantRepository;

    UserService userService;

    CartMapper cartMapper;

    private void calculatorTotalPrice(Cart cart) {
        // Nếu giỏ hàng rỗng
        if (cart.getCartItems().isEmpty()) {
            cart.setTotalPrice(0L);
            return;
        }
        // Tính toán tổng tiền giỏ hàng
        long totalPrice = cart.getCartItems().stream()
                .mapToLong(item -> {
                    long price = item.getVariant() != null
                            ? item.getVariant().getPrice()
                            : item.getProduct().getPrice();
                    return price * item.getQuantity();
                })
                .sum();

        cart.setTotalPrice(totalPrice);
    }

    public CartResponse addProductToCart(CartRequest request) {
        // Lấy user đang đăng nhập
        User user = userService.getCurrentUser();

        // Kiểm tra nếu giỏ hàng đã tồn tại
        Optional<Cart> cartOptional = cartRepository.findByUserId(user.getId());
        Cart cart;

        int quantity = request.getQuantity();

        if (cartOptional.isPresent()) {
            cart = cartOptional.get();

            // Kiểm tra sản phẩm và variant trong giỏ hàng
            Optional<CartItem> existingItemOptional = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getId().equals(request.getProduct_id())
                            && ((request.getVariant_id() == null && item.getVariant() == null)
                                    || (item.getVariant() != null
                                            && item.getVariant().getId().equals(request.getVariant_id()))))
                    .findFirst();

            if (existingItemOptional.isPresent()) {
                // Nếu sản phẩm đã có trong giỏ hàng, tăng số lượng
                CartItem existingItem = existingItemOptional.get();
                int newQuantity = existingItem.getQuantity() + quantity;

                // Kiểm tra tồn kho
                if (existingItem.getVariant() != null) {
                    if (existingItem.getVariant().getStockQuantity() < newQuantity) {
                        throw new AppException(ErrorCode.OUT_OF_STOCK);
                    }
                } else {
                    if (existingItem.getProduct().getStockQuantity() < newQuantity) {
                        throw new AppException(ErrorCode.OUT_OF_STOCK);
                    }
                }

                existingItem.setQuantity(newQuantity);
            } else {
                // Nếu sản phẩm hoặc variant chưa có trong giỏ hàng, thêm sản phẩm mới
                Product product = productRepository
                        .findByIdAndIsDeletedFalse(request.getProduct_id())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                Variant variant = null;
                if (!product.getVariants().isEmpty()) {
                    // Nếu sản phẩm có variants, yêu cầu người dùng chọn variant
                    if (request.getVariant_id() == null) {
                        throw new AppException(ErrorCode.VARIANT_INVALID);
                    }

                    // Kiểm tra variant đã chọn có tồn tại không
                    variant = variantRepository
                            .findByIdAndProductId(request.getVariant_id(), request.getProduct_id())
                            .orElseThrow(() -> new AppException(ErrorCode.VARIANT_INVALID));

                    // Kiểm tra tồn kho của Variant
                    if (variant.getStockQuantity() < quantity) {
                        throw new AppException(ErrorCode.OUT_OF_STOCK);
                    }
                } else {
                    // Nếu sản phẩm không có variants, không cần chọn variant
                    if (product.getStockQuantity() < quantity) {
                        throw new AppException(ErrorCode.OUT_OF_STOCK);
                    }
                }

                CartItem newItem = CartItem.builder()
                        .product(product)
                        .variant(variant)
                        .quantity(quantity)
                        .build();
                cart.getCartItems().add(newItem);
            }
        } else {
            // Nếu giỏ hàng chưa tồn tại, tạo giỏ hàng mới
            Product product = productRepository
                    .findByIdAndIsDeletedFalse(request.getProduct_id())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            Variant variant = null;
            if (!product.getVariants().isEmpty()) {
                // Nếu sản phẩm có variants, yêu cầu người dùng chọn variant
                if (request.getVariant_id() == null) {
                    throw new AppException(ErrorCode.VARIANT_INVALID);
                }

                // Kiểm tra variant đã chọn có tồn tại không
                variant = variantRepository
                        .findByIdAndProductId(request.getVariant_id(), request.getProduct_id())
                        .orElseThrow(() -> new AppException(ErrorCode.VARIANT_INVALID));

                // Kiểm tra tồn kho của Variant
                if (variant.getStockQuantity() < quantity) {
                    throw new AppException(ErrorCode.OUT_OF_STOCK);
                }
            } else {
                // Nếu sản phẩm không có variants, không cần chọn variant
                if (product.getStockQuantity() < quantity) {
                    throw new AppException(ErrorCode.OUT_OF_STOCK);
                }
            }

            CartItem newItem = CartItem.builder()
                    .product(product)
                    .variant(variant)
                    .quantity(quantity)
                    .build();

            cart = Cart.builder()
                    .user(user)
                    .cartItems(Collections.singleton(newItem))
                    .build();
        }

        this.calculatorTotalPrice(cart);

        // Chuyển đổi Cart thành CartResponse để trả về
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    private Cart createEmptyCart(User user) {
        return Cart.builder().user(user).cartItems(Collections.emptySet()).build();
    }

    public CartResponse getCart() {
        // Lấy user đang đăng nhập
        User user = userService.getCurrentUser();

        // Lấy giỏ hàng của user
        Cart cart = cartRepository.findByUserId(user.getId()).orElse(this.createEmptyCart(user));

        this.calculatorTotalPrice(cart);

        // Chuyển đổi Cart thành CartResponse để trả về
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse updateCart(CartRequest request) {
        // Lấy user đang đăng nhập
        User user = userService.getCurrentUser();

        // Lấy giỏ hàng của user
        Cart cart =
                cartRepository.findByUserId(user.getId()).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Tìm sản phẩm trong giỏ hàng
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProduct_id()))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND_IN_CART));

        // Cập nhật variant nếu cần
        if (request.getVariant_id() != null) {
            Variant variant = variantRepository
                    .findById(request.getVariant_id())
                    .orElseThrow(() -> new AppException(ErrorCode.VARIANT_INVALID));
            cartItem.setVariant(variant);
        }

        // Cập nhật số lượng
        cartItem.setQuantity(request.getQuantity());

        this.calculatorTotalPrice(cart);

        // Trả về CartResponse sau khi xóa sản phẩm
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    public CartResponse removeProductFromCart(CartRequest request) {
        // Lấy user đang đăng nhập
        User user = userService.getCurrentUser();

        // Lấy giỏ hàng của user
        Cart cart =
                cartRepository.findByUserId(user.getId()).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Tìm sản phẩm trong giỏ hàng
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProduct_id()))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND_IN_CART));

        // Xóa CartItem này khỏi giỏ hàng
        cart.getCartItems().remove(cartItem);

        this.calculatorTotalPrice(cart);

        // Trả về CartResponse sau khi xóa sản phẩm
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }
}
