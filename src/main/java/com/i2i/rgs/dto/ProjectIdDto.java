package com.i2i.rgs.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectIdDto {

    private String projectId;
    private String projectName;
}
