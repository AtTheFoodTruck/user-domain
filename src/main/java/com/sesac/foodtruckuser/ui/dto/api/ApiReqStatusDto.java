package com.sesac.foodtruckuser.ui.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiReqStatusDto {

    @JsonProperty("b_no")
    private List<String> bNo = new ArrayList<>();  // 사업자 등록 번호

    @Builder
    public ApiReqStatusDto(String bNo) {
        this.bNo.add(bNo);
    }
}
