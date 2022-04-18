package com.sesac.foodtruckuser.ui.controller;

import com.sesac.foodtruckuser.application.security.jwt.JwtTokenProvider;
import com.sesac.foodtruckuser.application.service.UserService;
import com.sesac.foodtruckuser.application.service.redis.RedisService;
import com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity.User;
import com.sesac.foodtruckuser.infrastructure.persistence.mysql.repository.UserRepository;
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
public class UserApiController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;
    private final Response response;

    /**
     * 개인 회원가입
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-26
    **/
    @Operation(summary = "회원가입", responses = {
                    @ApiResponse(
                            responseCode = "200", description = "ok"
                            , content = @Content(schema = @Schema(implementation = UserResponseDto.JoinUserDto.class)))
            })
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = UserResponseDto.JoinUserDto.class, message = "ok", code=200)
    )
    @ApiOperation(value = "개인 회원가입")
    @PostMapping("/users/v1/join")
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
    @PostMapping("/users/v1/owner/join")
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
    @PostMapping("/users/v1/logins")
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

        // id 추가
        String email = authentication.getName();
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("email 해당하는 회원이 존재하지 않습니다 " + email));
        Long userId = findUser.getId();

        HttpHeaders httpHeaders = new HttpHeaders();

        // Header 에 추가
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        // jwt 토큰
        return response.successToken(new UserResponseDto.TokenDto(accessToken, refreshToken, userId), "로그인에 성공했습니다.", httpHeaders, HttpStatus.OK);
    }

    /**
     * 로그아웃
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @ApiOperation(value = "로그아웃")
    @PostMapping("/users/v1/logout")
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
    @PostMapping("/users/v1/refresh")
    public ResponseEntity<?> updateRefreshToken(@Valid @RequestBody UserRequestDto.UpdateTokenDto updateTokenDto, BindingResult results) {

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
    @PatchMapping("/users/v1/mypage/name")
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
    @PatchMapping("/users/v1/mypage/password")
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
    @PostMapping("/users/v1/validation/email")
    public ResponseEntity<?> validateDuplicateEmail(@RequestBody UserRequestDto.DuplicateEmail duplicateEmail) {

        return userService.validateDuplicateEmail(duplicateEmail.getEmail());
    }

    /**
     * 닉네임 중복 체크
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @ApiOperation(value = "닉네임 중복 체크")
    @PostMapping("/users/v1/validation/name")
    public ResponseEntity<?> validateDuplicateUsername(@Valid @RequestBody UserRequestDto.UpdateNameDto updateNameDto, BindingResult results) {

        // validation 검증
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }
        return userService.validateDuplicateUser(updateNameDto.getUsername());
    }
}
