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

    @Transactional
    public ResponseEntity<?> signUpUser(UserRequestDto.JoinUserDto user) {

        if (userRepository.findByUsername(user.getUsername()).orElse(null) != null) {
            return response.fail("이미 가입되어 있는 유저입니다.", HttpStatus.BAD_REQUEST);
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User createdUser = User.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .authorities(Collections.singleton(authority))
                .phoneNum(user.getPhoneNum())
                .activated(true)
                .build();

        User savedUser = userRepository.save(createdUser);

        return response.success(new UserResponseDto.JoinUserDto(savedUser), "회원가입에 성공했습니다.", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> signUpManager(@Valid UserRequestDto.JoinManagerDto manager) {

        if (userRepository.findByUsername(manager.getUsername()).orElse(null) != null) {
            return response.fail("이미 가입되어 있는 유저입니다.", HttpStatus.BAD_REQUEST);
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_MANAGER")
                .build();

        User createdManager = User.builder()
                .email(manager.getEmail())
                .username(manager.getUsername())
                .password(passwordEncoder.encode(manager.getPassword()))
                .phoneNum(manager.getPhoneNum())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .bNo(manager.getBNo())
                .build();

        User savedUser = userRepository.save(createdManager);

        return response.success(new UserResponseDto.JoinUserDto(savedUser), "회원가입에 성공했습니다.", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updateUsername(String email, UserRequestDto.UpdateNameDto updateNameDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        if (StringUtils.hasText(updateNameDto.getUsername())) {
            user.changeUser(updateNameDto.getUsername());
        }

        UserRequestDto.UpdateNameDto nameDto = new UserRequestDto.UpdateNameDto();
        nameDto.setUsername(user.getUsername());

        return response.success(nameDto, "닉네임 수정이 완료되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updatePassword(String email, UserRequestDto.UpdatePwDto updatePwDto) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        if (StringUtils.hasText(updatePwDto.getCurrentPassword())) {
            if (!passwordEncoder.matches(updatePwDto.getCurrentPassword(), user.getPassword())) {
                return response.fail("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
            }
        }

        user.encodingPassword(passwordEncoder.encode(updatePwDto.getNewPassword()));

        return response.success("비밀번호 변경이 완료되었습니다.");
    }

    public ResponseEntity<?> validateDuplicateEmail(String email) {
        int findUsers = userRepository.countByEmail(email);

        if (findUsers > 0) {
            return response.fail("이메일이 중복되었습니다.", HttpStatus.BAD_REQUEST);
        }

        return response.success("사용 가능한 이메일입니다.");
    }

    public ResponseEntity<?> validateDuplicateUser(String username) {
        int findUsers = userRepository.countByUsername(username);

        if (findUsers > 0) {
            return response.fail("닉네임이 중복되었습니다.", HttpStatus.BAD_REQUEST);
        }

        return response.success("사용 가능한 닉네임입니다.");
    }

    @Transactional
    public ResponseEntity<?> logout(UserRequestDto.LogoutUserDto logoutDto) {
        if (!jwtTokenProvider.validateToken(logoutDto.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(logoutDto.getAccessToken());

        if (redisTemplate.opsForValue().get("email:" + authentication.getName()) != null) {
            redisTemplate.delete("email:" + authentication.getName());
        }

        Long expiration = jwtTokenProvider.getExpiration(logoutDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(logoutDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }

    @Transactional
    public ResponseEntity<?> updateRefreshToken(UserRequestDto.UpdateTokenDto tokenDto) {

        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        String refreshTokenFromDB =
                redisService.getRefreshToken(authentication.getName());

        if (ObjectUtils.isEmpty(refreshTokenFromDB)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        String newAccessToken = jwtTokenProvider.createToken(authentication, false);

        String email = authentication.getName();
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("email 해당하는 회원이 존재하지 않습니다 " + email));
        Long userId = findUser.getId();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + newAccessToken);

        return response.successToken(new UserResponseDto.TokenDto(newAccessToken, refreshToken, userId), "", httpHeaders, HttpStatus.OK);
    }
}
