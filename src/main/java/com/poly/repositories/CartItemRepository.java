package com.poly.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {}
