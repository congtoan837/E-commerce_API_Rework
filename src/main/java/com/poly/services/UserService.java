package com.poly.services;

import java.util.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.poly.dto.request.UserRequest;
import com.poly.dto.response.UserResponse;
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
        return userMapper.toUserResponseList(
                userRepository.findByIsDeletedFalse(pageable).getContent());
    }

    public UserResponse update(UserRequest request) {
        User user =
                userRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUserFromUserRequest(user, request);

        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(Set.copyOf(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getInfo(String id) {
        User user =
                userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    public Optional<User> getByUsername(String value) {
        return userRepository.findByUsername(value);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void delete(UUID Id) {
        User user =
                userRepository.findByIdAndIsDeletedFalse(Id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        user.setDeleted(true);

        userRepository.save(user);
    }
}
