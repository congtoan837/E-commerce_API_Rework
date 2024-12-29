package com.poly.services;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.poly.dto.request.UserRequest;
import com.poly.dto.response.PageResponse;
import com.poly.dto.response.user.UserResponse;
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

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponse create(UserRequest request) {
        User user = userMapper.toUser(request);

        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public PageResponse<UserResponse> getAll(String keyword, int page, int size) {
        if (StringUtils.isNotBlank(keyword))
            keyword = StringUtils.lowerCase(keyword).trim();

        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageResult = userRepository.searchByKeywordWithoutUsername("admin", keyword, pageable);
        return userMapper.toPageResponse(pageResult);
    }

    public Page<User> getUser() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "createTime"));
        return userRepository.findAll(pageable);
    }

    public UserResponse update(UserRequest request) {
        User user =
                userRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUserFromUserRequest(user, request);

        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getInfo() {
        return userMapper.toUserResponse(getCurrentUser());
    }

    public Optional<User> getByUsername(String value) {
        return userRepository.findByUsername(value);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsernameAndIsDeletedFalse(username);
    }

    public boolean delete(UUID id) {
        User user = userRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setDeleted(true);
        userRepository.save(user);

        return Boolean.TRUE;
    }
}
