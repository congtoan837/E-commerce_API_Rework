package com.poly.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.poly.dto.request.CartRequest;
import com.poly.dto.response.CartResponse;
import com.poly.entity.Cart;
import com.poly.entity.CartItem;
import com.poly.entity.Product;
import com.poly.entity.User;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.mapper.CartMapper;
import com.poly.repositories.CartRepository;
import com.poly.repositories.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;

    UserService userService;

    CartMapper cartMapper;

    public CartResponse addProductToCart(CartRequest request) {
        // Lấy user đang đăng nhập
        User user = userService.getCurrentUser();

        // Kiểm tra nếu giỏ hàng đã tồn tại
        Optional<Cart> cartOptional = cartRepository.findByUserId(user.getId());
        Cart cart;

        if (cartOptional.isPresent()) {
            // Nếu giỏ hàng đã tồn tại, lấy giỏ hàng hiện có
            cart = cartOptional.get();

            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            Optional<CartItem> existingItemOptional = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getId().equals(request.getProduct_id()))
                    .findFirst();

            if (existingItemOptional.isPresent()) {
                // Nếu sản phẩm đã có trong giỏ hàng, tăng số lượng
                CartItem existingItem = existingItemOptional.get();
                existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            } else {
                // Nếu sản phẩm chưa có trong giỏ hàng, thêm sản phẩm mới
                Product product = productRepository
                        .findByIdAndIsDeletedFalse(request.getProduct_id())
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

                CartItem newItem = CartItem.builder()
                        .product(product)
                        .quantity(request.getQuantity())
                        .cart(cart) // Gán giỏ hàng cho sản phẩm
                        .build();

                cart.getCartItems().add(newItem);
            }
        } else {
            // Nếu giỏ hàng chưa tồn tại, tạo giỏ hàng mới với sản phẩm
            Product product = productRepository
                    .findByIdAndIsDeletedFalse(request.getProduct_id())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

            CartItem newItem = CartItem.builder()
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();

            Set<CartItem> items = new HashSet<>();
            items.add(newItem);

            cart = Cart.builder().user(user).cartItems(items).build();

            newItem.setCart(cart); // Liên kết CartItem với giỏ hàng mới
        }

        // Chuyển đổi Cart thành CartResponse để trả về
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    public CartResponse getCart() {
        // Lấy user đang đăng nhập
        User user = userService.getCurrentUser();

        // Lấy giỏ hàng của user
        Cart cart =
                cartRepository.findByUserId(user.getId()).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Chuyển đổi Cart thành CartResponse để trả về
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse removeProductFromCart(CartRequest request) {
        // Lấy user đang đăng nhập
        User user = userService.getCurrentUser();

        // Lấy giỏ hàng của user
        Cart cart =
                cartRepository.findByUserId(user.getId()).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Tìm CartItem chứa sản phẩm cần xóa trong giỏ hàng
        Optional<CartItem> itemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProduct_id()))
                .findFirst();

        if (itemToRemove.isPresent()) {
            // Nếu CartItem tồn tại, xóa CartItem này khỏi giỏ hàng
            cart.getCartItems().remove(itemToRemove.get());

            // Lưu lại giỏ hàng sau khi xóa CartItem
            cartRepository.save(cart);
        } else {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND_IN_CART);
        }

        // Trả về CartResponse sau khi xóa sản phẩm
        return cartMapper.toCartResponse(cart);
    }
}
