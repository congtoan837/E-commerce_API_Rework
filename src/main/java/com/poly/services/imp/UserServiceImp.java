package com.poly.services.imp;

import com.poly.entity.User;
import com.poly.repositories.UserRepository;
import com.poly.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getById(UUID id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByVerifyCode(String verifyCode) {
        return userRepository.getByVerifyCode(verifyCode);
    }

    @Override
    @Modifying
    @Transactional
    public void findByVerifyCodeAndEnable(String code) {
        userRepository.findByVerifyCodeAndEnable(code);
    }

    @Override
    public Page<User> getAllUser(String user, Pageable pageable) {
        return userRepository.getAllUser(user, pageable);
    }

    @Override
    public <S extends User> S save(S entity) {
        return userRepository.save(entity);
    }

    @Override
    public void deleteById(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
