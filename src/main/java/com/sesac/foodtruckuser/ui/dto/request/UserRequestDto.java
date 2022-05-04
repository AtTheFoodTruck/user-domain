package com.sesac.foodtruckuser.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserRequestDto {

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JoinUserDto {

        @Email(message = "이메일 형식을 지켜주세요")
        @NotBlank(message = "이메일 정보는 필수입니다.")
        private String email;

        @NotBlank(message = "닉네임 정보는 필수입니다.")
        private String username; // 닉네임

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @NotBlank(message = "핸드폰 정보는 필수입니다.")
        private String phoneNum;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JoinManagerDto {

        @NotBlank(message = "이메일 정보는 필수입니다.")
        @Email
        private String email;

        @NotBlank(message = "닉네임 정보는 필수입니다.")
        private String username;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @NotBlank(message = "핸드폰 정보는 필수입니다.")
        @JsonProperty("phone_num")
        private String phoneNum;

        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @JsonProperty("b_no")
        private String bNo;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginUserDto {

        @NotBlank(message = "이메일 정보는 필수입니다.")
        @Email
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LogoutUserDto {

        @NotBlank(message = "잘못된 요청입니다.")
        private String accessToken;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateNameDto {
        @JsonIgnore
        private Long userId;

        @NotBlank(message = "닉네임을 입력해주세요")
        private String username;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdatePwDto {

        @NotBlank(message = "현재 비밀번호를 입력해주세요")
        private String currentPassword;

        @NotBlank(message = "새로운 비밀번호를 입력해주세요")
        private String newPassword;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    @AllArgsConstructor
    @Data
    public static class UserDto {
        private Long id;
        private String email;
        private String username;
        private String password;
        private String phoneNum;
        private boolean active;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    public static class DuplicateEmail {
        private String email;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    public static class DuplicateNickname {
        private String username;
    }

    @Schema(description = "Token 갱신 정보")
    @AllArgsConstructor
    @Getter
    public class UpdateTokenDto {
        @Schema(description = "Access Token")
        @NotBlank(message = "잘못된 요청입니다.")
        @JsonProperty("access_token")
        private String accessToken;

        @Schema(description = "Refresh Token")
        @NotBlank(message = "잘못된 요청입니다.")
        @JsonProperty("refresh_token")
        private String refreshToken;
    }

}
