package com.sesac.foodtruckuser.ui.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@AllArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String email;
    private String username;
    private String password;
    private String phoneNum;
    private boolean active;
}
