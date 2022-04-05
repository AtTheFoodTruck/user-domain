package com.sesac.foodtruckuser.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenDto {

    private String accessToken;
    private String refreshToken;
    private Long userId;
}