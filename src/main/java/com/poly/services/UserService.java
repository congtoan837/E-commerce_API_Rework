package com.poly.services;

import com.poly.entity.User;
import com.poly.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getImage())
                .phone(user.getPhone())
                .address(user.getAddress())
                .image(user.getImage())
                .enabled(user.isEnabled())
                .username(user.getUsername())
                .password(user.getPassword())
                .verifyCode(user.getVerifyCode())
                .roles(user.getRoles())
                .createTime(LocalDateTime.now())
                .modifiedLastTime(LocalDateTime.now())
                .build());
    }

    public Page<User> getAllUser(Pageable Pageable) {
        return userRepository.findAll(Pageable);
    }
}
