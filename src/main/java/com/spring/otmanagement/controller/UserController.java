package com.spring.otmanagement.controller;

import com.spring.otmanagement.dto.LoginRequest;
import com.spring.otmanagement.entity.User;
import com.spring.otmanagement.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/api/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
}
