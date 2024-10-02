package com.poly.controller;

import com.poly.dto.PaginationRequest;
import com.poly.dto.Request.UserRequest;
import com.poly.dto.Response.ApiResponse;
import com.poly.dto.Response.UserResponse;
import com.poly.entity.User;
import com.poly.ex.ModelMapperConfig;
import com.poly.ex.StringContent;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.exception.GlobalException;
import com.poly.services.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UsersController {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    PasswordEncoder passwordEncoder;
    UserService userService;
    ModelMapperConfig mapper;

    private static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @GetMapping("/getUser")
    public ApiResponse getAllUser(PaginationRequest paginationRequest) {
        Page<User> users = userService.getAll(paginationRequest);
        List<UserResponse> responses = mapper.mapList(users.getContent(), UserResponse.class);
        return GlobalException.AppResponse(responses);
    }

    @PostMapping("/createUser")
    public ApiResponse<?> createUser(@RequestBody UserRequest request) {
        if (request.getUsername().length() < 6)
            throw new AppException(ErrorCode.USERNAME_SHORT);
        if (request.getPassword().length() < 6)
            throw new AppException(ErrorCode.PASSWORD_SHORT);
        if (!validate(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_REGEX);

        if (userService.getByUsername(request.getUsername()) != null)
            throw new AppException(ErrorCode.USERNAME_EXITS);
        // encode password
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        // set default avatar if not input
        request.setImage(StringUtils.isNotBlank(request.getImage()) ? request.getImage() : StringContent.avatar_default);

        User user = mapper.map(request, User.class);

        UserResponse response = mapper.map(userService.save(user), UserResponse.class);

        return GlobalException.AppResponse(response);
    }

    @PutMapping("/updateUser")
    public ApiResponse<?> updateUser(@RequestBody UserRequest request) {
        if (StringUtils.isNotBlank(request.getPassword()) || StringUtils.isNotBlank(request.getEmail())) {
            if (request.getPassword().length() < 6)
                throw new AppException(ErrorCode.PASSWORD_SHORT);
            if (!validate(request.getEmail()))
                throw new AppException(ErrorCode.EMAIL_REGEX);
        }
        if (StringUtils.isNotBlank(request.getPassword()))
            request.setPassword(passwordEncoder.encode(request.getPassword()));

        userService.getById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User user = mapper.map(request, User.class);

        UserResponse response = mapper.map(userService.save(user), UserResponse.class);

        return GlobalException.AppResponse(response);
    }

    @DeleteMapping("/deleteUser")
    public ApiResponse<?> deleteUser(@RequestBody UserRequest request) {
            userService.getById(request.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            return GlobalException.AppResponse(userService.deletedById(request.getId()));
    }
}
