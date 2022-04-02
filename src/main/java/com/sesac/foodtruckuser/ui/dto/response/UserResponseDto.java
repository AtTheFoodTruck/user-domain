package com.sesac.foodtruckuser.ui.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "응답")
public class UserResponseDto {
    /**
     * 회원가입 응답
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
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
        // manager
        @Schema(description = "bNo")
        private String bNo;

        // Entity -> Dto
        public JoinUserDto(User joinMember) {
            this.username = joinMember.getUsername();
            this.email = joinMember.getEmail();
            this.phoneNum = joinMember.getPhoneNum();
            this.bNo = joinMember.getBNo();
        }
    }

}