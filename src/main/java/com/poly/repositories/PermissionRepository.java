package com.poly.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
