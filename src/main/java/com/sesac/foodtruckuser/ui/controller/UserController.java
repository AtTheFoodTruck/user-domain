package com.sesac.foodtruckuser.ui.controller;

import com.sesac.foodtruckuser.application.security.jwt.JwtTokenProvider;
import com.sesac.foodtruckuser.application.service.UserService;
import com.sesac.foodtruckuser.application.service.redis.RedisService;
import com.sesac.foodtruckuser.ui.dto.*;
import com.sesac.foodtruckuser.ui.dto.request.UserRequestDto;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @Timed(value="users.status", longTask = true)
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", message=" + env.getProperty("greeting.message")
                + ", token secret=" + env.getProperty("jwt.secret"));
    }

    /**
     * 개인 회원가입
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-26
    **/
    @ApiOperation(value = "개인 회원가입")
    @ApiResponses({
            @ApiResponse(code = 201, message = "회원가입에 성공했습니다."),
            @ApiResponse(code = 400, message = "이미 가입되어 있는 유저입니다.")
    })
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
    @ApiResponses({
            @ApiResponse(code = 201, message = "회원가입에 성공했습니다."),
            @ApiResponse(code = 400, message = "이미 가입되어 있는 유저입니다.")
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그인에 성공했습니다."),
//            @ApiResponse(code = 400, message = "이미 가입되어 있는 유저입니다.")
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그아웃 되었습니다."),
            @ApiResponse(code = 400, message = "잘못된 요청입니다.")
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "토큰 갱신에 성공했습니다."),
            @ApiResponse(code = 400, message = "Refresh Token 정보가 유효하지 않습니다."),
            @ApiResponse(code = 400, message = "잘못된 요청입니다.")
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "닉네임 수정이 완료되었습니다."),
//            @ApiResponse(code = 400, message = "잘못된 요청입니다.")
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "비밀번호 변경이 완료되었습니다."),
            @ApiResponse(code = 400, message = "비밀번호가 일치하지 않습니다.")
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "이메일 중복 검증되었습니다."),
            @ApiResponse(code = 400, message = "이메일이 중복되었습니다.")
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "닉네임 중복 검증되었습니다."),
            @ApiResponse(code = 400, message = "닉네임이 중복되었습니다.")
    })
    @PostMapping("/validation/name")
    public ResponseEntity<?> validateDuplicateUsername(@RequestBody UserDto userDto) {
        return userService.validateDuplicateUser(userDto.getUsername());
    }
}
