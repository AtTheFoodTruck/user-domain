package com.sesac.foodtruckuser.ui.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

public class UserResponseDto {

    /**
     * 회원가입 응답
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @AllArgsConstructor
    public static class JoinUserDto {

        private String username;
        private String email;
        private String phoneNum;
        // manager
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