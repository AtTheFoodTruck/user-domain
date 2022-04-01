package com.sesac.foodtruckuser.ui.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 사업자등록번호 상태 조회 Request
 * @author jjaen
 * @version 1.0.0
 * 작성일 2022/03/29
**/
//@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
