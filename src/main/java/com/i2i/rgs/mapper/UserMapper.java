package com.i2i.rgs.mapper;

import com.i2i.rgs.dto.CreateUserDto;
import com.i2i.rgs.dto.UserResponseDto;
import com.i2i.rgs.model.User;

public class UserMapper {

    public static User createDtoToModel(CreateUserDto user) {
        return User.builder()
              //  .eid(user.getEid())
                .email(user.getEmail())
              //  .name(user.getName())
                .isFinance(user.getIsFinance())
                .build();
    }

    public static UserResponseDto modelToResponseDto(User user) {
        return UserResponseDto.builder()
                .eid(user.getEid())
                .email(user.getEmail())
                .name(user.getName())
                .isFinance(user.getIsFinance())
               // .project(ProjectMapper.modelToDto(user.getProject()))
                .build();
    }
}
