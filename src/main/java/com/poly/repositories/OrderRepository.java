package com.poly.repositories;

import com.poly.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(byte status);

    @Query("SELECT od FROM Order AS od WHERE " +
            "od.orderPhone LIKE %:user% OR " +
            "od.orderAddress LIKE %:user% OR " +
            "od.user.username LIKE %:user% OR " +
            "od.user.address LIKE %:user% OR " +
            "od.user.name LIKE %:user%")
    Page<Order> getAllOrder(String user, Pageable pageable);

    Order getById(Long id);

    List<Order> findByUserId(UUID userId);
}
