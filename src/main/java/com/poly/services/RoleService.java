package com.poly.services;

import com.poly.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    Role findByName(String name);

    List<Role> findAll();

    Page<Role> findAll(Pageable pageable);

    <S extends Role> S save(S entity);

    Optional<Role> findById(Long aLong);

    void deleteById(Long aLong);
}
