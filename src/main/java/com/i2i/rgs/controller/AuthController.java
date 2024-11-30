package com.i2i.rgs.controller;

import java.util.Map;
import java.util.Set;

import com.i2i.rgs.dto.CreateUserDto;
import com.i2i.rgs.dto.LoginDto;
import com.i2i.rgs.helper.SuccessResponse;
import com.i2i.rgs.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

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
        Map<String, Object> response = userService.addUser(user);
        return SuccessResponse.setSuccessResponseCreated("User Registration Successful", response);
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
    public ResponseEntity<SuccessResponse> login(@RequestBody LoginDto user) {
        Map<String, Object> response = userService.authenticateUser(user);
        return SuccessResponse.setSuccessResponseOk("User authenticated successfully", response);
    }

    @PatchMapping("/assign-projects")
    public ResponseEntity<SuccessResponse> assignProjects(@RequestBody CreateUserDto user) {
        userService.assignProjects(user.getEmail(), user.getProjects());
        return SuccessResponse.setSuccessResponseOk("Projects assigned successfully", null);
    }
}