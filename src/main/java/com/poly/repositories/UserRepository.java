package com.poly.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    boolean existsByUsernameAndIsDeletedFalse(String username);

    @Query("SELECT u FROM User u WHERE u.id <> :id AND u.isDeleted = false " + "AND (u.username LIKE %:keyword% OR "
            + "u.name LIKE %:keyword% OR "
            + "u.email LIKE %:keyword% OR "
            + "u.phone LIKE %:keyword% OR "
            + "u.address LIKE %:keyword%)")
    Page<User> searchByKeyword(@Param("id") UUID id, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(u.id) FROM User u WHERE u.id <> :id AND u.isDeleted = false "
            + "AND (u.username LIKE %:keyword% OR "
            + "u.name LIKE %:keyword% OR "
            + "u.email LIKE %:keyword% OR "
            + "u.phone LIKE %:keyword% OR "
            + "u.address LIKE %:keyword%)")
    long countByKeyword(@Param("id") UUID id, @Param("keyword") String keyword);

    Page<User> findByIdNotAndIsDeletedFalseAndNameContaining(UUID id, String keyword, Pageable pageable);

    Optional<User> findByIdAndIsDeletedFalse(UUID id);
}
