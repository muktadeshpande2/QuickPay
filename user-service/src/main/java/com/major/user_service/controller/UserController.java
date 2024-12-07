package com.major.user_service.controller;

import com.major.user_service.dto.CreateUserRequest;
import com.major.user_service.dto.GetUserResponse;
import com.major.user_service.model.User;
import com.major.user_service.service.UserService;
import com.major.user_service.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * SignUp API
     * @param createUserRequest
     */
    @PostMapping("/create")
    public void createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        userService.createUser(Utils.convertCreateUserRequest(createUserRequest));
    }

    /**
     * Fetching profile information
     */
    @GetMapping("/profile-info")
    public GetUserResponse getProfile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userService.getById(user.getId());
        return Utils.convertToGetUserResponse(user);
    }
}
