package com.major.user_service.utils;

import com.major.user_service.dto.CreateUserRequest;
import com.major.user_service.dto.GetUserResponse;
import com.major.user_service.model.User;

public class Utils {

    public static User convertCreateUserRequest(CreateUserRequest createUserRequest) {
        return User.builder()
                .username(createUserRequest.getMobileNumber())
                .name(createUserRequest.getName())
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
                .age(createUserRequest.getAge())
                .build();
    }

    public static GetUserResponse convertToGetUserResponse(User user) {
        return GetUserResponse.builder()
                .name(user.getName())
                .age(user.getAge())
                .mobileNumber(user.getUsername())
                .email(user.getEmail())
                .createdOn(user.getCreatedOn())
                .updatedOn(user.getUpdatedOn())
                .build();
    }
}
