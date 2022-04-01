package com.sesac.foodtruckuser.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class UpdateTokenDto {
    @NotBlank(message = "잘못된 요청입니다.")
    private String accessToken;

    @NotBlank(message = "잘못된 요청입니다.")
    private String refreshToken;

}
