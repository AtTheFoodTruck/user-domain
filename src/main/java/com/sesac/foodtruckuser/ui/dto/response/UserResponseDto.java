package com.sesac.foodtruckuser.ui.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Schema(description = "응답")
public class UserResponseDto {

    @Schema(description = "로그인 응답")
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @AllArgsConstructor
    public static class JoinUserDto {
        @Schema(description = "username")
        private String username;
        @Schema(description = "email")
        private String email;
        @Schema(description = "phoneNum")
        private String phoneNum;
        @Schema(description = "bNo")
        private String bNo;

        public JoinUserDto(User joinMember) {
            this.username = joinMember.getUsername();
            this.email = joinMember.getEmail();
            this.phoneNum = joinMember.getPhoneNum();
            this.bNo = joinMember.getBNo();
        }
    }
    @AllArgsConstructor
    @Getter
    public static class TokenDto {

        private String accessToken;
        private String refreshToken;
        private Long userId;
    }

}