package com.sesac.foodtruckuser.ui.controller;

import com.sesac.foodtruckuser.application.service.api.BNoApiRestTemplate;
import com.sesac.foodtruckuser.ui.dto.Helper;
import com.sesac.foodtruckuser.ui.dto.Response;
import com.sesac.foodtruckuser.ui.dto.request.BNoApiRequestDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/managers")
@RestController
public class BusinessApiController {

    private final BNoApiRestTemplate apiRestTemplate;
    private final Response response;

    @GetMapping("/health_check")
    public String health() {
        return "health_check";
    }
    @ApiOperation(value = "사업자등록번호 상태조회")
    @PostMapping("/status")
    public ResponseEntity<?> bNoStatus(@RequestBody BNoApiRequestDto.BNoStatusDto statusDto, BindingResult results) {

        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        if (!apiRestTemplate.statusApi(statusDto)){
            return response.fail("인증 실패", HttpStatus.BAD_REQUEST);
        }

        return response.success( "인증 성공");
    }

    @ApiOperation(value = "사업자등록번호 진위여부")
    @PostMapping("/validate")
    public ResponseEntity<?> bNoValidate(@RequestBody BNoApiRequestDto.BNoValidateDto BNoValidateDto, BindingResult results) {

        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        if (!apiRestTemplate.validateApi(BNoValidateDto)){
            return response.fail( "인증 실패", HttpStatus.BAD_REQUEST);
        }

        return response.success( "인증 성공");
    }
}
