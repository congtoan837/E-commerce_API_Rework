package com.poly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import com.poly.dto.request.UserRequest;
import com.poly.dto.response.PageResponse;
import com.poly.dto.response.UserResponse;
import com.poly.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    User toUser(UserRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    void updateUserFromUserRequest(@MappingTarget User user, UserRequest request);

    default PageResponse<UserResponse> toPageResponse(Page<User> userPage) {
        return PageResponse.<UserResponse>builder()
                .totalPages(userPage.getTotalPages())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .data(userPage.map(this::toUserResponse).getContent())
                .build();
    }
}
