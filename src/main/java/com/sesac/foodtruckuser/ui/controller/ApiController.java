package com.sesac.foodtruckuser.ui.controller;

import com.sesac.foodtruckuser.application.service.api.BNoApiRestTemplate;
import com.sesac.foodtruckuser.ui.dto.Helper;
import com.sesac.foodtruckuser.ui.dto.Response;
import com.sesac.foodtruckuser.ui.dto.request.BNoApiRequestDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Api(value = "사업자등록번호 조회 API")
@RequiredArgsConstructor
@RequestMapping("/managers")
@RestController
public class ApiController {

    private final BNoApiRestTemplate apiRestTemplate;
    private final Response response;

    @GetMapping("/health_check")
    public String health() {
        return "health_check";
    }

    /**
     * 사업자등록번호 상태조회
     * validation check, return type 수정 - jaemin
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
    **/
    @ApiOperation(value = "사업자등록번호 상태조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "인증 성곰"),
            @ApiResponse(code = 400, message = "인증 실패")
    })
    @PostMapping("/status")
    public ResponseEntity<?> bNoStatus(@RequestBody BNoApiRequestDto.BNoStatusDto statusDto, BindingResult results) {

        // validation check
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        if (!apiRestTemplate.statusApi(statusDto)){
            return response.fail("인증 실패", HttpStatus.BAD_REQUEST);
        }

        return response.success( "인증 성공");
    }

    /**
     * 사업자등록번호 진위여부
     * validation check, return type 수정 - jaemin
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
    **/
    @ApiOperation(value = "사업자등록번호 진위여부")
    @ApiResponses({
            @ApiResponse(code = 200, message = "인증 성곰"),
            @ApiResponse(code = 400, message = "인증 실패")
    })
    @PostMapping("/validate")
    public ResponseEntity<?> bNoValidate(@RequestBody BNoApiRequestDto.BNoValidateDto BNoValidateDto, BindingResult results) {

        // validation check
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        if (!apiRestTemplate.validateApi(BNoValidateDto)){
            return response.fail( "인증 실패", HttpStatus.BAD_REQUEST);
        }

        return response.success( "인증 성공");
    }
}
