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

    @Query("SELECT u FROM User u WHERE u.username <> :username AND u.isDeleted = false "
            + "AND (u.username ILIKE %:keyword% OR "
            + "u.name ILIKE %:keyword% OR remove_accent(u.name) ILIKE %:keyword% OR "
            + "u.email ILIKE %:keyword% OR "
            + "u.phone ILIKE %:keyword% OR "
            + "u.address ILIKE %:keyword% OR remove_accent(u.address) ILIKE %:keyword%)")
    Page<User> searchByKeywordWithoutUsername(
            @Param("username") String username, @Param("keyword") String keyword, Pageable pageable);

    Optional<User> findByIdAndIsDeletedFalse(UUID id);
}
