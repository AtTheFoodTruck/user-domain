package com.sesac.foodtruckuser.ui.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sesac.foodtruckuser.ui.dto.request.BNoApiRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ApiReqValidateDto {

    private List<InnerDto> businesses = new ArrayList<>();  // 사업자 등록 번호

    @Getter
    @NoArgsConstructor
    public static class InnerDto {
        @JsonProperty("b_no")
        private String bNo;                         // 사업자 등록 번호
        @JsonProperty("start_dt")
        private String startDt;                     // 개업일
        @JsonProperty("p_nm")
        private String pNm;                         // 대표자사명

        public InnerDto(BNoApiRequestDto.BNoValidateDto bNoValidateDto) {
            this.bNo = bNoValidateDto.getBNo();
            this.startDt = bNoValidateDto.getStartDt();
            this.pNm = bNoValidateDto.getPNm();
        }
    }
    @Builder
    public ApiReqValidateDto(BNoApiRequestDto.BNoValidateDto bNoValidateDto) {
        this.businesses.add(new InnerDto(bNoValidateDto));
    }
}
