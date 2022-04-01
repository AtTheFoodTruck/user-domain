package com.sesac.foodtruckuser.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BNoApiResponseDto {
    /**
     * 사업자등록번호 상태조회, 진위여부 응답
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BNoDto {
        private boolean valid;
        private String message;
    }
}
