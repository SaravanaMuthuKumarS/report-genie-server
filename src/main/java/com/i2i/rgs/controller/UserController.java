package com.i2i.rgs.controller;

import java.util.Map;

import com.i2i.rgs.dto.CreateUserDto;
import com.i2i.rgs.helper.SuccessResponse;
import com.i2i.rgs.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * <p>
     * Adds a new user to the database.
     * </p>
     *
     * @param user The user details to be added.
     * @return {@link SuccessResponse} with {@link HttpStatus} CREATED
     */
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> addUser(@Validated @RequestBody CreateUserDto user) {
        userService.addUser(user);
        return SuccessResponse.setSuccessResponseCreated("User Registration Successful", null);
    }

    /**
     * <p>
     * Authenticates a user and returns the token for the user.
     * </p>
     *
     * @param user The user details to be authenticated.
     * @return {@link SuccessResponse} containing the token with {@link HttpStatus} OK
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody CreateUserDto user) {
        String token = userService.authenticateUser(user);
        return SuccessResponse.setSuccessResponseOk("User authenticated successfully", Map.of("token", token));
    }
}