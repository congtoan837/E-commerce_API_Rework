package com.poly.services;

import com.poly.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService{
    List<Order> findByUserId(Long userId);

    List<Order> findByOrderStatus(Long orderStatus);

    List<Order> findOrderByUser(String user);

    Order getOne(Long aLong);

    Page<Order> findAll(Pageable pageable);

    Order save(Order entity);

    Optional<Order> findById(Long aLong);

    long count();

    void deleteById(Long aLong);
}
