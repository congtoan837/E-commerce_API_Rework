package com.poly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import com.poly.dto.request.OrderRequest;
import com.poly.dto.response.PageResponse;
import com.poly.dto.response.order.OrderResponse;
import com.poly.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(OrderRequest request);

    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toUserResponseList(List<Order> categories);

    void updateOrderFromOrderRequest(@MappingTarget Order order, OrderRequest request);

    default PageResponse<OrderResponse> toPageResponse(Page<Order> page) {
        return PageResponse.<OrderResponse>builder()
                .totalPages(page.getTotalPages())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .data(page.map(this::toOrderResponse).getContent())
                .build();
    }
}
