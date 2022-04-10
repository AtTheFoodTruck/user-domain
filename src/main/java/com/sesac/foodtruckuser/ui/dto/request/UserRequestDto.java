package com.sesac.foodtruckuser.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserRequestDto {

    /**
     * 개인 회원가입 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
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

    /**
     * 점주 회원가입 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
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
        private String username; //닉네임

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @NotBlank(message = "핸드폰 정보는 필수입니다.")
        @JsonProperty("phone_num")
        private String phoneNum;

        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @JsonProperty("b_no")
        private String bNo;
    }

    /**
     * 로그인 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
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

    /**
     * 로그아웃 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LogoutUserDto {

        @NotBlank(message = "잘못된 요청입니다.")
        private String accessToken;
    }

    /**
     * 이름 업데이트 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateNameDto {

        @NotBlank(message = "닉네임을 입력해주세요")
        private String username;
    }

    /**
     * 비밀번호 업데이트 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
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
}
