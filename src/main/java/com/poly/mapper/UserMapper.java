package com.poly.mapper;

import com.poly.dto.Request.UserRequest;
import com.poly.dto.Response.UserResponse;
import com.poly.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

    @Mapping(target = "roles", ignore = true)
    void updateUserFromUserRequest(@MappingTarget User user, UserRequest request);
}
