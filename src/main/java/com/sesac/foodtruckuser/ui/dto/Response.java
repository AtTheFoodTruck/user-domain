package com.sesac.foodtruckuser.ui.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@Schema(description = "로그인 요청")
@Component
public class Response {

    @Getter
    @Builder
    private static class Body {
        private int state;
        private String result;
        private String message;
        private Object data;
        private Object error;

    }
    /**
     * 성공 응답 메서드
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-30
    **/
    public ResponseEntity<?> success(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .state(status.value())
                .data(data)
                .result("success")
                .message(msg)
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }

    /********************************************************************************************************************/
    /**
     * 헤더를 추가해서 성공 응답 반환
     *     {
     *         "state" : 200,
     *         "result" : success,
     *         "message" : message,
     *         "data" : [],
     *         "error" : []
     *     }
     * @author jaemin
     **/
    public ResponseEntity<?> successToken(Object data, String message, HttpHeaders headers, HttpStatus status) {
        Body body = Body.builder()
                .state(status.value())
                .data(data)
                .result("success")
                .message(message)
                .error(Collections.emptyList())
                .build();

        return new ResponseEntity(body, headers, status);
    }


    /**
     * 메세지만 가진 성공 응답 반환 (오버로딩)
     *     {
     *         "state" : 200,
     *         "result" : success,
     *         "message" : message,
     *         "data" : [],
     *         "error" : []
     *     }
     * @author jaemin
     **/
    public ResponseEntity<?> success(String msg) {
        return success(Collections.emptyList(), msg, HttpStatus.OK);
    }

    /**
     * 데이터만 가진 성공 응답 반환 (오버로딩)
     *     {
     *         "state" : 200,
     *         "result" : success,
     *         "message" : null,
     *         "data" : [{data1}, {data2}...],
     *         "error" : []
     *     }
     * @author jaemin
     **/
    public ResponseEntity<?> success(Object data) {
        return success(data, null, HttpStatus.OK);
    }

    /**
     * 성공 응답만 반환 (오버로딩)
     *     {
     *         "state" : 200,
     *         "result" : success,
     *         "message" : null,
     *         "data" : [{data1}, {data2}...],
     *         "error" : []
     *     }
     * @author jaemin
     **/
    public ResponseEntity<?> success() {
        return success(Collections.emptyList(), null, HttpStatus.OK);
    }

/**********************************************************************************************************************/
    /**
     * 실패 응답 메서드
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-30
    **/
    public ResponseEntity<?> fail(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .state(status.value())
                .data(data)
                .result("fail")
                .message(msg)
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }

    /**
     * 메세지를 가진 실패 응답 반환 (오버로딩)
     * {
     * "state" : HttpStatus Code,
     * "result" : fail,
     * "message" : message,
     * "data" : [],
     * "error" : [{error1}, {error2}...]
     * }
     *
     * @author jaemin
     **/
    public ResponseEntity<?> fail(String msg, HttpStatus status) {
        return fail(Collections.emptyList(), msg, status);
    }

/********************************************************************************************************************/
    /**
     * Validation 체크
     *
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-30
     **/
    public ResponseEntity<?> invalidFields(LinkedList<LinkedHashMap<String, String>> errors) {
        Body body = Body.builder()
                .state(HttpStatus.BAD_REQUEST.value())
                .data(Collections.emptyList())
                .result("fail")
                .message("")
                .error(errors)
                .build();
        return ResponseEntity.ok(body);
    }
}
