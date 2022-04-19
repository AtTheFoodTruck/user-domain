package com.sesac.foodtruckuser.ui.controller;

import com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity.User;
import com.sesac.foodtruckuser.infrastructure.persistence.mysql.repository.UserRepository;
import com.sesac.foodtruckuser.infrastructure.query.http.dto.StoreInfo;
import com.sesac.foodtruckuser.ui.dto.Result;
import com.sesac.foodtruckuser.ui.dto.response.CreateUserDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    /**
     * Request From Item Dommain - 회원정보 조회
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-04
     **/
    @ApiOperation(value = "Item Domain에서 요청 - 회원정보 조회")
    @GetMapping("/api/v1/users/{userId}")
    public CreateUserDto userInfo(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                                  @PathVariable Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("id 해당하는 회원이 존재하지 않습니다 " + userId)
        );

        return new CreateUserDto(user);
    }

    /**
     * Request From Item Domain - user정보에 storeId 저장
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-09
    **/
    @ApiOperation(value = "Item Domain에서 요청 - user정보 저장")
    @PostMapping("/api/v1/stores")
    @Transactional
    public void saveStoreInfo(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                              @RequestBody StoreInfo storeInfo) {
        User user = userRepository.findById(storeInfo.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("id 해당하는 회원이 존재하지 않습니다.)")
        );

        user.setStoreId(storeInfo.getStoreId());
    }
    /**7
     * Request From Order Domain - 점주 주문 조회 페이지
     * userId, userName
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/04/12
    **/
    @ApiOperation(value = "Order Domain에서 요청 - 점주 주문 조회 페이지")
    @GetMapping("/api/v1/info/{userIds}")
    public ResponseEntity<Result> getUsers(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                                           @PathVariable("userIds") Iterable<Long> userIds) {

        List<User> findUsers = userRepository.findAllById(userIds);

        List<CreateUserDto.UserInfoResponse> userInfoResponses = findUsers.stream()
                .map(user -> CreateUserDto.UserInfoResponse.of(user))
                .collect(Collectors.toList());

        return ResponseEntity.ok(Result.createSuccessResult(userInfoResponses));
    }
    
    @GetMapping("/api/v1/ping")
    public ResponseEntity<String> healthCheck() {
        log.info("###health check");
        return ResponseEntity.ok("pong");
    }
}
