package com.poly.services;

import com.poly.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService{

    List<Order> findByStatus(byte status);

    Page<Order> getAllOrder(String user, Pageable pageable);

    Order getById(Long id);

    List<Order> findByUserId(UUID userId);

    <S extends Order> S save(S entity);

    void deleteById(Long aLong);
}
