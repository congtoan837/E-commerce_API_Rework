package com.poly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.poly.dto.request.CartRequest;
import com.poly.dto.response.CartResponse;
import com.poly.entity.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart toCart(CartRequest request);

    CartResponse toCartResponse(Cart cart);

    List<CartResponse> toUserResponseList(List<Cart> categories);

    void updateCartFromCartRequest(@MappingTarget Cart cart, CartRequest request);
}
