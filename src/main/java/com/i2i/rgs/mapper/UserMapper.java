package com.i2i.rgs.mapper;

import java.util.stream.Collectors;

import com.i2i.rgs.dto.CreateUserDto;
import com.i2i.rgs.dto.UserResponseDto;
import com.i2i.rgs.model.User;

public class UserMapper {

    public static User createDtoToModel(CreateUserDto user) {
        return User.builder()
                .email(user.getEmail())
                .name(user.getFullName())
                .isFinance(user.getIsFinance())
                .build();
    }

    public static UserResponseDto modelToResponseDto(User user) {
        return UserResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .isFinance(user.getIsFinance())
                .projects(user.getProjects().stream().map(ProjectMapper::modelToDto).collect(Collectors.toSet()))
                .build();
    }
}
