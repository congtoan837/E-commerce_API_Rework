package com.poly.services;

import com.poly.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface UserService {

    User getById(UUID id);

    User getByUsername(String username);

    User getByVerifyCode(String verifyCode);

    @Modifying
    @Transactional
    void findByVerifyCodeAndEnable(String code);

    Page<User> getAllUser(String user, Pageable pageable);

    <S extends User> S save(S entity);

    void deleteById(UUID uuid);

    List<User> findAll();
}
