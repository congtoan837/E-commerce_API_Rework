package com.poly.services;

import com.poly.dto.Request.UserRequest;
import com.poly.dto.Response.UserResponse;
import com.poly.entity.Permission;
import com.poly.entity.Role;
import com.poly.entity.User;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.mapper.UserMapper;
import com.poly.repositories.RoleRepository;
import com.poly.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;

    UserMapper userMapper;

    public UserResponse create(UserRequest request) {
        User user = userMapper.toUser(request);

        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(Set.copyOf(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userMapper.toUserResponseList(userRepository.findByIsDeletedFalse(pageable).getContent());
    }

    public Optional<User> getById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> getById(String id) {
        return userRepository.findById(UUID.fromString(id));
    }

    public Optional<User> getByUsername(String value) {
        return userRepository.findByUsername(value);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void delete(UUID Id) {
        User user = userRepository.findByIdAndIsDeletedFalse(Id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        user.setDeleted(true);

        userRepository.save(user);
    }
}
