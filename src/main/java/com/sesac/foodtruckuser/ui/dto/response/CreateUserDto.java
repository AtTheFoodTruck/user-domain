package com.sesac.foodtruckuser.ui.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "타 도메인 유저 객체 응답")
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUserDto {
    private Long userId;
    private String email;
    private String username;
    private String phoneNum;
    private boolean activated;
    private String bNo; // 사업자등록번호

    // entity -> dto
    public CreateUserDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.phoneNum = user.getPhoneNum();
        this.activated = user.isActivated();
        this.bNo = user.getBNo();
    }

    /**
     * CreateUserDto 의 설명 적기
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/04/12
     **/
    @Data
    public static class UserInfoResponse {
        private Long userId;
        private String userName;

        public static UserInfoResponse of(User user) {
            UserInfoResponse userInfoResponse = new UserInfoResponse();
            userInfoResponse.userId = user.getId();
            userInfoResponse.userName = user.getUsername();

            return userInfoResponse;
        }
    }
}
