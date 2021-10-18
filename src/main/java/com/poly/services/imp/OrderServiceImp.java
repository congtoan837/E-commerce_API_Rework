package com.poly.services.imp;

import com.poly.entity.Order;
import com.poly.repositories.OrderRepository;
import com.poly.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Override
    public List<Order> findByStatus(byte status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public Page<Order> getAllOrder(String user, Pageable pageable) {
        return orderRepository.getAllOrder(user, pageable);
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.getById(id);
    }

    @Override
    public List<Order> findByUserId(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public <S extends Order> S save(S entity) {
        return orderRepository.save(entity);
    }

    @Override
    public void deleteById(Long aLong) {
        orderRepository.deleteById(aLong);
    }
}
