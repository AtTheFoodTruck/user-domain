package com.sesac.foodtruckuser.application.service;

import com.sesac.foodtruckuser.application.security.jwt.JwtTokenProvider;
import com.sesac.foodtruckuser.application.service.redis.RedisService;
import com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity.Authority;
import com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity.User;
import com.sesac.foodtruckuser.infrastructure.persistence.mysql.repository.UserRepository;
import com.sesac.foodtruckuser.ui.dto.Response;
import com.sesac.foodtruckuser.ui.dto.request.UserRequestDto;
import com.sesac.foodtruckuser.ui.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final RedisService redisService;
    private final Response response;

    /**
     * 개인 회원 회원가입
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-28
     *
     * @param user*/
    @Transactional
    public ResponseEntity<?> signUpUser(UserRequestDto.JoinUserDto user) {
        // 중복회원 검증
//        validateDuplicateUser(user.getUsername());

        if (userRepository.findByUsername(user.getUsername()).orElse(null) != null) {
            return response.fail("이미 가입되어 있는 유저입니다.", HttpStatus.BAD_REQUEST);
        }

        // User 권한 생성
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User createdUser = User.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        // User 객체 생성
        User savedUser = userRepository.save(createdUser);

        return response.success(new UserResponseDto.JoinUserDto(savedUser), "회원가입에 성공했습니다.", HttpStatus.CREATED);
    }

    /**
     * 점주 회원가입
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     *
     * @param manager*/
    @Transactional
    public ResponseEntity<?> signUpManager(@Valid UserRequestDto.JoinManagerDto manager) {
        // 중복 체크
//        validateDuplicateUser(manager.getUsername());
//        validateDuplicateEmail(manager.getEmail());

        if (userRepository.findByUsername(manager.getUsername()).orElse(null) != null) {
            return response.fail("이미 가입되어 있는 유저입니다.", HttpStatus.BAD_REQUEST);
        }

        // Manager 권한 생성
        Authority authority = Authority.builder()
                .authorityName("ROLE_MANAGER")
                .build();

        // Manager 객체 생성
        User createdManager = User.builder()
                .email(manager.getEmail())
                .username(manager.getUsername())
                .password(passwordEncoder.encode(manager.getPassword()))
                .phoneNum(manager.getPhoneNum())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .bNo(manager.getBNo())
                .build();

        // Manager 객체 저장
        User savedUser = userRepository.save(createdManager);

        // toDTO
        return response.success(new UserResponseDto.JoinUserDto(savedUser), "회원가입에 성공했습니다.", HttpStatus.CREATED);
    }

    /**
     * 사용자 정보 수정 -닉네임 [유저, 매니저]
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-28
     **/
    @Transactional
    public ResponseEntity<?> updateUsername(String email, UserRequestDto.UpdateNameDto updateNameDto) {
        // 요청 유저 정보 조회
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        // 유저 정보 수정(닉네임)
        if (StringUtils.hasText(updateNameDto.getUsername())) {
            user.changeUser(updateNameDto.getUsername());
        }

        UserRequestDto.UpdateNameDto nameDto = new UserRequestDto.UpdateNameDto();
        nameDto.setUsername(user.getUsername());

        return response.success(nameDto, "닉네임 수정이 완료되었습니다.", HttpStatus.OK);
    }

    /**
     * 사용자 정보 수정 -비밀번호 [유저, 매니저]
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    @Transactional
    public ResponseEntity<?> updatePassword(String email, UserRequestDto.UpdatePwDto updatePwDto) {

        // 요청 유저 정보 조회
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        // 유저 정보 수정(비밀번호)
        if (StringUtils.hasText(updatePwDto.getCurrentPassword())) {
            if (!passwordEncoder.matches(updatePwDto.getCurrentPassword(), user.getPassword())) {
                return response.fail("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
            }
        }

        // 비밀번호 업데이트
        user.encodingPassword(passwordEncoder.encode(updatePwDto.getNewPassword()));

        return response.success("비밀번호 변경이 완료되었습니다.");
    }

    /**
     * 중복 회원 검증 - 이메일
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    public ResponseEntity<?> validateDuplicateEmail(String email) {
        int findUsers = userRepository.countByEmail(email);

        if (findUsers > 0) {
            return response.fail("이메일이 중복되었습니다.", HttpStatus.BAD_REQUEST);
        }

        return response.success("사용 가능한 이메일입니다.");
    }

    /**
     * 중복 회원 검증 - 닉네임
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    public ResponseEntity<?> validateDuplicateUser(String username) {
        int findUsers = userRepository.countByUsername(username);

        if (findUsers > 0) {
            return response.fail("닉네임이 중복되었습니다.", HttpStatus.BAD_REQUEST);
        }

        return response.success("사용 가능한 닉네임입니다.");
    }

    /**
     * 로그아웃
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    @Transactional
    public ResponseEntity<?> logout(UserRequestDto.LogoutUserDto logoutDto) {
        // 1. AccessToken 검증
        if (!jwtTokenProvider.validateToken(logoutDto.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. AccessToken에서 email을 가져옴
        Authentication authentication = jwtTokenProvider.getAuthentication(logoutDto.getAccessToken());

        // 3. Redis에서 해당 email로 저장된 Refresh Token이 있는지 여부를 확인 후 있을 경우 삭제
        if (redisTemplate.opsForValue().get("email:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("email:" + authentication.getName());
        }

        // 4. 해당 AccessToken 유효시간을 갖고와서 BlackList로 저장
        Long expiration = jwtTokenProvider.getExpiration(logoutDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(logoutDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }

    /**
     * accessToken 재발급
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    @Transactional
    public ResponseEntity<?> updateRefreshToken(UserRequestDto.UpdateTokenDto tokenDto) {

        // 1. dto에서 토큰 추출
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        // 2. refresh 토큰 validation 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 3. Authentication 객체 추출
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // 4. redis 에서 Refresh 토큰 value 추출
        String refreshTokenFromDB =
                redisService.getRefreshToken(authentication.getName());

        // 5. 시간이 만료되어 db 에 없거나, 로그아웃 된 토큰인지 검증
        if (ObjectUtils.isEmpty(refreshTokenFromDB)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 6. 새로운 토큰 생성
        String newAccessToken = jwtTokenProvider.createToken(authentication, false);

        // id 추가
        String email = authentication.getName();
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("email 해당하는 회원이 존재하지 않습니다 " + email));
        Long userId = findUser.getId();

        // 새로운 access token Headers 에 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + newAccessToken);

        return response.successToken(new UserResponseDto.TokenDto(newAccessToken, refreshToken, userId), "", httpHeaders, HttpStatus.OK);
    }

    /**
     * 중복 가게명 검증
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    //TODO

}
