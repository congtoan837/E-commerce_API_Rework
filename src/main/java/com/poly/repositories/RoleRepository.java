package com.poly.repositories;

import com.poly.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Page<Role> findByIsDeletedFalse(Pageable pageable);

    Optional<Role> findByNameAndIsDeletedFalse(String Name);
}
