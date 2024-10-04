package com.poly.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {}
