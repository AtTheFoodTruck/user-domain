package com.sesac.foodtruckuser.ui.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result<T> {
    private String message;
    private T data;

    @Builder
    public Result(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static Result createErrorResult(String message) {
        return Result.builder()
                .message(message)
                .data(null)
                .build();
    }

    // 해당 <T> 는 클래스의 T와 다름
    public static <T> Result createSuccessResult(T data) {
        return Result.builder()
                .message("")
                .data(data)
                .build();
    }

    public static Result success(){
        return Result.builder()
                .message("성공")
                .data(null)
                .build();
    }
}
