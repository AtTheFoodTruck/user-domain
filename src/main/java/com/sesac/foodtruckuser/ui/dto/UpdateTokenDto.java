package com.sesac.foodtruckuser.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

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
