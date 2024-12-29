package com.poly.mapper;

import org.mapstruct.Mapper;

import com.poly.dto.response.cart.CartResponse;
import com.poly.entity.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponse toCartResponse(Cart cart);
}
