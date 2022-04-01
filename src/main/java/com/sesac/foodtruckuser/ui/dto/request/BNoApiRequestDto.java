package com.sesac.foodtruckuser.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class BNoApiRequestDto {

    /**
     * 사업자등록번호 상태 조회 요청
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
     **/
    @ApiModel(value = "사업자 등록번호")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BNoStatusDto {

        @ApiModelProperty(value = "사업자 등록번호")
        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @Length(min = 10, max = 10, message = "사업자 등록번호는 10자리 입니다.")  // 10 자리
        @JsonProperty("b_no")
        private String bNo;  // 사업자 등록 번호
    }

    /**
     * 사업자등록번호 진위 확인 요청
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
     **/
    @ApiModel(value = "사업자 등록 정보")
    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BNoValidateDto {
        @ApiModelProperty(value = "사업자 등록번호")
        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @Length(min = 10, max = 10, message = "사업자 등록번호는 10자리 입니다.")  // 10 자리
        @JsonProperty("b_no")
        private String bNo;

        @ApiModelProperty(value = "개업일")
        @NotBlank(message = "개업일은 필수입니다.")
        @JsonProperty("start_dt")
        private String startDt;

        @ApiModelProperty(value = "대표자사명")
        @NotBlank(message = "대표자사명은 필수입니다.")
        @JsonProperty("p_nm")
        private String pNm;
    }
}