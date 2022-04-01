package com.sesac.foodtruckuser.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel(value = "개인 회원가입 정보")
    @Data
    @Builder
    @AllArgsConstructor
    public static class JoinUserDto {

        @ApiModelProperty(value = "이메일", required = true)
        @Email(message = "이메일 형식을 지켜주세요")
        @NotBlank(message = "이메일 정보는 필수입니다.")
        private String email;

        @ApiModelProperty(value = "닉네임", required = true)
        @NotBlank(message = "닉네임 정보는 필수입니다.")
        private String username; // 닉네임

        @ApiModelProperty(value = "비밀번호", required = true)
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @ApiModelProperty(value = "핸드폰 번호", required = true)
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
    @ApiModel(value = "점주 회원가입 정보")
    @Data
    @AllArgsConstructor
    public static class JoinManagerDto {
        @ApiModelProperty(value = "이메일", required = true)
        @NotBlank(message = "이메일 정보는 필수입니다.")
        @Email
        private String email;

        @ApiModelProperty(value = "닉네임", required = true)
        @NotBlank(message = "닉네임 정보는 필수입니다.")
        private String username; //닉네임

        @ApiModelProperty(value = "비밀번호", required = true)
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @ApiModelProperty(value = "등록번호", required = true)
        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @Length(min = 10, max = 10, message = "사업자 등록번호는 10자리 입니다.")  // 10
        @JsonProperty("b_no")
        private String bNo;

        @ApiModelProperty(value = "핸드폰 번호", required = true)
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
    @ApiModel(value = "로그인 정보")
    @Data
    @AllArgsConstructor
    public static class LoginUserDto {

        @ApiModelProperty(value = "이메일", required = true)
        @NotBlank(message = "이메일 정보는 필수입니다.")
        @Email
        private String email;

        @ApiModelProperty(value = "비밀번호", required = true)
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;
    }

    /**
     * 로그아웃 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @ApiModel(value = "로그아웃 정보")
    @Getter
    @Setter
    @AllArgsConstructor
    public static class LogoutUserDto {

        @ApiModelProperty(value = "Access Token", required = true)
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
    @ApiModel(value = "닉네임 수정 정보")
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateNameDto {

        @ApiModelProperty(value = "닉네임", required = true)
        @NotBlank(message = "닉네임을 입력해주세요")
        private String username;
    }

    /**
     * 비밀번호 업데이트 요청
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @ApiModel(value = "비밀번호 수정 정보")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdatePwDto {

        @ApiModelProperty(value = "현재 비밀번호", required = true)
        @NotBlank(message = "현재 비밀번호를 입력해주세요")
        @JsonProperty("current_password")
        private String currentPassword;

        @ApiModelProperty(value = "새로운 비밀번호", required = true)
        @NotBlank(message = "새로운 비밀번호를 입력해주세요")
        @JsonProperty("new_password")
        private String newPassword;
    }
}
