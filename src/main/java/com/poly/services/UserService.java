package com.poly.services;

import com.poly.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserService {


    Page<User> findAll(Pageable pageable);

    User getByUsername(String username);

    User save(User entity);

    List<User> saveAll(List<User> entities);

    Optional<User> findById(Long aLong);

    void deleteById(Long aLong);

    Iterable<User> findAll();

    long count();

    void delete(User entity);

    void deleteAll(List<User> entities);

    void deleteAll();
}
