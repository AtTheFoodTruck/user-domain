package com.sesac.foodtruckuser.ui.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

//@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateUserDto {

    @NotBlank(message = "닉네임을 입력해주세요")
    private String username;

    @NotBlank(message = "현재 비밀번호를 입력해주세요")
    private String currentPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요")
    private String newPassword;
}
