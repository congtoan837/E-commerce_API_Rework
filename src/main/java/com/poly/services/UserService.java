package com.poly.services;

import com.poly.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getByUser(String username);

    User save(User entity);

    List<User> saveAll(List<User> entities);

    Optional<User> findById(Integer integer);

    boolean existsById(Integer integer);

    Iterable<User> findAll();

    Iterable<User> findAllById(Iterable<Integer> integers);

    long count();

    void deleteById(Integer integer);

    void delete(User entity);

    void deleteAll(List<User> entities);

    void deleteAll();
}
