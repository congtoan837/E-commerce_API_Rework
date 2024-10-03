package com.poly.repositories;

import com.poly.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Page<Permission> findByIsDeletedFalse(Pageable pageable);

    Optional<Permission> findByNameAndIsDeletedFalse(String Id);
}
