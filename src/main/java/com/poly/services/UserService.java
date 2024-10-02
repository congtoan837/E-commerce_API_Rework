package com.poly.services;

import com.poly.dto.PaginationRequest;
import com.poly.entity.User;
import com.poly.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Page<User> getAll(PaginationRequest request) {
        Sort sort = request.getSortDirection() == 1
                ? Sort.by(Sort.Direction.ASC, request.getSortBy())
                : Sort.by(Sort.Direction.DESC, request.getSortBy());

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        return userRepository.findAll(pageable);
    }

    public Optional<User> getById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> getByUsername(String value) {
        return userRepository.findByUsername(value);
    }

    public boolean deletedById(UUID uuid) {
        userRepository.save(User.builder()
                .id(uuid)
                .isDeleted(true)
                .build()
        );
        return true;
    }
}
