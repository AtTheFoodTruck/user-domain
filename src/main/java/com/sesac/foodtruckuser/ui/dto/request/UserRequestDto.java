package com.sesac.foodtruckuser.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserRequestDto {

    /**
     * 개인 회원가입 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
    **/
    @Schema(description = "개인 회원가입 정보")
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JoinUserDto {

        @Schema(description = "이메일", required = true)
        @Email(message = "이메일 형식을 지켜주세요")
        @NotBlank(message = "이메일 정보는 필수입니다.")
        private String email;

        @Schema(description = "닉네임", required = true)
        @NotBlank(message = "닉네임 정보는 필수입니다.")
        private String username; // 닉네임

        @Schema(description = "비밀번호", required = true)
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @Schema(description = "핸드폰 번호", required = true)
        @NotBlank(message = "핸드폰 정보는 필수입니다.")
        @JsonProperty("phone_num")
        private String phoneNum;
    }

    /**
     * 점주 회원가입 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @Schema(description = "점주 회원가입 정보")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JoinManagerDto {
        @Schema(description = "이메일", required = true)
        @NotBlank(message = "이메일 정보는 필수입니다.")
        @Email
        private String email;

        @Schema(description = "닉네임", required = true)
        @NotBlank(message = "닉네임 정보는 필수입니다.")
        private String username; //닉네임

        @Schema(description = "비밀번호", required = true)
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @Schema(description = "등록번호", required = true)
        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @Length(min = 10, max = 10, message = "사업자 등록번호는 10자리 입니다.")  // 10
        @JsonProperty("b_no")
        private String bNo;

        @Schema(description = "핸드폰 번호", required = true)
        @NotBlank(message = "핸드폰 정보는 필수입니다.")
        @JsonProperty("phone_num")
        private String phoneNum;
    }

    /**
     * 로그인 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @Schema(description = "로그인 정보")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginUserDto {

        @Schema(description = "이메일", required = true)
        @NotBlank(message = "이메일 정보는 필수입니다.")
        @Email
        private String email;

        @Schema(description = "비밀번호", required = true)
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;
    }

    /**
     * 로그아웃 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @Schema(description = "로그아웃 정보")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LogoutUserDto {

        @Schema(description = "Access Token", required = true)
        @NotBlank(message = "잘못된 요청입니다.")
        @JsonProperty("access_token")
        private String accessToken;
    }

    /**
     * 이름 업데이트 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @Schema(description = "닉네임 수정 정보")
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateNameDto {

        @Schema(description = "닉네임", required = true)
        @NotBlank(message = "닉네임을 입력해주세요")
        private String username;
    }

    /**
     * 비밀번호 업데이트 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @Schema(description = "비밀번호 수정 정보")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdatePwDto {

        @Schema(description = "현재 비밀번호", required = true)
        @NotBlank(message = "현재 비밀번호를 입력해주세요")
        @JsonProperty("current_password")
        private String currentPassword;

        @Schema(description = "새로운 비밀번호", required = true)
        @NotBlank(message = "새로운 비밀번호를 입력해주세요")
        @JsonProperty("new_password")
        private String newPassword;
    }
}
