package com.sesac.foodtruckuser.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;

public class BNoApiRequestDto {

    @Schema(description = "사업자 등록번호")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BNoStatusDto {

        @Schema(description = "사업자 등록번호")
        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @Length(min = 10, max = 10, message = "사업자 등록번호는 10자리 입니다.")  // 10 자리
        @JsonProperty("b_no")
        private String bNo;  // 사업자 등록 번호
    }

    @Schema(description = "사업자 등록 정보")
    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BNoValidateDto {
        @Schema(description = "사업자 등록번호")
        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @Length(min = 10, max = 10, message = "사업자 등록번호는 10자리 입니다.")  // 10 자리
        @JsonProperty("b_no")
        private String bNo;

        @Schema(description = "개업일")
        @NotBlank(message = "개업일은 필수입니다.")
        @JsonProperty("start_dt")
        private String startDt;

        @Schema(description = "대표자사명")
        @NotBlank(message = "대표자사명은 필수입니다.")
        @JsonProperty("p_nm")
        private String pNm;
    }
}
