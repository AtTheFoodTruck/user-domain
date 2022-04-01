package com.sesac.foodtruckuser.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class BNoApiRequestDto {

    /**
     * 사업자등록번호 상태 조회 요청
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
     **/
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BNoStatusDto {

        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @JsonProperty("b_no")
        private String bNo;  // 사업자 등록 번호
    }

    /**
     * 사업자등록번호 진위 확인 요청
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
     **/
    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BNoValidateDto {
        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @JsonProperty("b_no")
        private String bNo;                         // 사업자 등록 번호
        @NotBlank(message = "개업일은 필수입니다.")
        @JsonProperty("start_dt")
        private String startDt;                     // 개업일
        @NotBlank(message = "대표자사명은 필수입니다.")
        @JsonProperty("p_nm")
        private String pNm;                         // 대표자사명
    }
}
