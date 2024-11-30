package com.i2i.rgs.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginDto {
    private String username;
    private String password;
}
