package com.poly.repositories;

import com.poly.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT od FROM Order AS od WHERE od.user.id = :userId")
    List<Order> findByUserId(Long userId);

    @Query("SELECT od FROM Order AS od WHERE od.Status = :orderStatus")
    List<Order> findByOrderStatus(Long orderStatus);

    @Query("SELECT od FROM Order AS od WHERE " +
            "od.OrderPhone LIKE %:user% OR " +
            "od.OrderAddress LIKE %:user% OR " +
            "od.user.username LIKE %:user% OR " +
            "od.user.address LIKE %:user% OR " +
            "od.user.name LIKE %:user%")
    List<Order> findOrderByUser(String user);
}
