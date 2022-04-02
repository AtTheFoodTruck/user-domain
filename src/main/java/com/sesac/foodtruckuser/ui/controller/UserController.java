package com.sesac.foodtruckuser.ui.controller;

import com.sesac.foodtruckuser.application.security.jwt.JwtTokenProvider;
import com.sesac.foodtruckuser.application.service.UserService;
import com.sesac.foodtruckuser.application.service.redis.RedisService;
import com.sesac.foodtruckuser.ui.dto.*;
import com.sesac.foodtruckuser.ui.dto.request.UserRequestDto;
import com.sesac.foodtruckuser.ui.dto.response.UserResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Tag(name = "users", description = "유저 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;
    private final Response response;
    private final Environment env;


    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service"
                + ", message=" + env.getProperty("greeting.message")
                + ", token secret=" + env.getProperty("jwt.secret"));
    }

    /**
     * 개인 회원가입
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-26
    **/
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "회원가입에 성공했습니다.",
//                    content = @Content(schema = @Schema(implementation = UserResponseDto.JoinUserDto.class))),
//            @ApiResponse(responseCode = "400", description = "이미 가입되어 있는 유저입니다.",
//                    content = @Content(schema = @Schema(implementation = Response.class))),
//    })
    @Operation(tags = "users", summary = "로그인",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "ok"
                            , content = @Content(schema = @Schema(implementation = UserResponseDto.JoinUserDto.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = UserResponseDto.JoinUserDto.class, message = "ok", code=200)
    )
    @PostMapping("/users/join")
    public ResponseEntity<?> signUpUser(@Valid @RequestBody UserRequestDto.JoinUserDto userDto, BindingResult results) {

        log.info("개인 회원가입");

        // validation 검증
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.signUpUser(userDto);
    }

    /**
     * 점주 회원가입
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @ApiOperation(value = "점주 회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "이미 가입되어 있는 유저입니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
    })
    @PostMapping("/managers/join")
    public ResponseEntity<?> signUpManager(@Valid @RequestBody UserRequestDto.JoinManagerDto managerDto, BindingResult results) {

        log.info("점주 회원가입");

        // validation 검증
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.signUpManager(managerDto);
    }

    /**
     * 로그인
     * 사용자, 점주
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-27
    **/
    // 로그인
    @ApiOperation(value = "로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(schema = @Schema(implementation = Response.class))),
//            @ApiResponse(code = 400, message = "")
    })
    @PostMapping("/logins")
    public ResponseEntity<?> authorize(@RequestBody UserRequestDto.LoginUserDto requestUser) {

        log.info("로그인 request");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestUser.getEmail(), requestUser.getPassword());

        // loadUserByUsername 메서드에서 리턴받은 user 객체로 Authentication 객체 생성
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 생성된 객체를 SecurityContext 에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 생성된 객체로 TokenProvider.createToken 메서드를 통해 jwt 토큰을 생성
        String accessToken = jwtTokenProvider.createToken(authentication, false);
        String refreshToken = jwtTokenProvider.createToken(authentication, true);

        // redis 에 저장
        redisService.setRefreshToken(requestUser.getEmail(), refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();

        // Header 에 추가
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        // jwt 토큰
        return response.successToken(new TokenDto(accessToken, refreshToken), "로그인에 성공했습니다.", httpHeaders, HttpStatus.OK);
    }

    /**
     * 로그아웃
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @ApiOperation(value = "로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 되었습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),

    })
    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody UserRequestDto.LogoutUserDto logoutDto, BindingResult results) {

        log.info("로그아웃");

        // validation 검증
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.logout(logoutDto);
    }

    /**
     * access token 갱신
     * 검증 로직 추가, 서비스로 로직 분리 - jaemin
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @ApiOperation(value = "토큰 갱신")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 갱신에 성공했습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Refresh Token 정보가 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
    })
    @PostMapping("/users/refresh")
    public ResponseEntity<?> updateRefreshToken(@Valid @RequestBody UpdateTokenDto updateTokenDto, BindingResult results) {

        log.info("Access Token 갱신");

        // validation 검증
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.updateRefreshToken(updateTokenDto);
    }

    /**
     * 회원 정보 수정(닉네임 변경)
     * 사용자, 점주
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-28
     **/
    @ApiOperation(value = "닉네임 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 수정이 완료되었습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "해당하는 유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
    })
    @PatchMapping("/name")
    public ResponseEntity<?> updateUsername(Principal principal,
                                      @Valid @RequestBody UserRequestDto.UpdateNameDto updateNameDto,
                                      BindingResult results) {
        log.info("회원정보수정 - 닉네임");

        // validation 검증
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.updateUsername(principal.getName(), updateNameDto);
    }

    /**
     * 회원 정보 수정(비밀번호 변경)
     * 사용자, 점주
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @ApiOperation(value = "비밀번호 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경이 완료되었습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
    })
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(Principal principal,
                                      @Valid @RequestBody UserRequestDto.UpdatePwDto updatePwDto,
                                      BindingResult results) {
        log.info("회원정보수정 - 비밀번호 변경");

        // validation 검증
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.updatePassword(principal.getName(), updatePwDto);
    }

    /**
     * 이메일 중복 체크
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    @ApiOperation(value = "이메일 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 중복 검증되었습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "이메일이 중복되었습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
    })
    @PostMapping("/validation/email")
    public ResponseEntity<?> validateDuplicateEmail(@RequestBody UserDto userDto) {

        return userService.validateDuplicateEmail(userDto.getEmail());
    }

    /**
     * 닉네임 중복 체크
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @ApiOperation(value = "닉네임 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 중복 검증되었습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "닉네임이 중복되었습니다.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
    })
    @PostMapping("/validation/name")
    public ResponseEntity<?> validateDuplicateUsername(@RequestBody UserDto userDto) {
        return userService.validateDuplicateUser(userDto.getUsername());
    }
}
