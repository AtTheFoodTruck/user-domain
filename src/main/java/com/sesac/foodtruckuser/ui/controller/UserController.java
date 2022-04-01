package com.sesac.foodtruckuser.ui.controller;

import com.sesac.foodtruckuser.application.security.jwt.JwtTokenProvider;
import com.sesac.foodtruckuser.application.service.UserService;
import com.sesac.foodtruckuser.application.service.redis.RedisService;
import com.sesac.foodtruckuser.ui.dto.*;
import com.sesac.foodtruckuser.ui.dto.request.UserRequestDto;
import io.micrometer.core.annotation.Timed;
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
    @PostMapping("/logins")
    public ResponseEntity<?> authorize(@RequestBody UserRequestDto.LoginUserDto requestUser) {

        log.info("로그인 request");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestUser.getEmail(), requestUser.getPassword());

        // loadUserByUsername메서드에서 리턴받은 user객체로 Authentication객체 생성
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 생성된 객체를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 생성된 객체로 TokenProvider.createToken 메서드를 통해 jwt토큰을 생성
        String accessToken = jwtTokenProvider.createToken(authentication, false);
        String refreshToken = jwtTokenProvider.createToken(authentication, true);

        // redis에 저장
        redisService.setRefreshToken(requestUser.getEmail(), refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();

        // Header에 추가
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        // jwt토큰
        return response.successToken(new TokenDto(accessToken, refreshToken), httpHeaders, HttpStatus.OK);
    }

    /**
     * 로그아웃
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
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
    @PostMapping("/validation/email")
    public ResponseEntity<?> validateDuplicateEmail(@RequestBody UserDto userDto) {

        return userService.validateDuplicateEmail(userDto.getEmail());
//        return new ResponseDto(HttpStatus.OK.value(), "이메일 중복 체크 성공");
    }

    /**
     * 닉네임 중복 체크
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @PostMapping("/validation/name")
    public ResponseEntity<?> validateDuplicateUsername(@RequestBody UserDto userDto) {
        return userService.validateDuplicateUser(userDto.getUsername());
    }
}
