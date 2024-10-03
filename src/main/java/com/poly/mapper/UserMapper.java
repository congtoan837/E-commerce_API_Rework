package com.poly.mapper;

import com.poly.dto.Request.UserRequest;
import com.poly.dto.Response.UserResponse;
import com.poly.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);

    void updateUserFromUserRequest(@MappingTarget User user, UserRequest request);
}
