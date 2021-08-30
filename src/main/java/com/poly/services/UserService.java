package com.poly.services;

import com.poly.dto.UserGetDto;
import com.poly.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long aLong);

    User getById(Long id);

    Page<User> getAllUser(String Username, Pageable pageable);

    User getByUsername(String username);

    User save(User entity);

    List<User> saveAll(List<User> entities);

    User getOne(Long aLong);

    void deleteById(Long aLong);

    Iterable<User> findAll();

    long count();

    void delete(User entity);

    void deleteAll(List<User> entities);

    void deleteAll();
}
