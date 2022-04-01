package com.sesac.foodtruckuser.application.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private static final long JWT_REFRESH_TOKEN_VALIDITY = 60 * 60 * 24 * 7;

    @Autowired
    private RedisTemplate<String, Object> tokenRedisTemplate;


    public void setRefreshToken(String email, String refreshToken) {
        tokenRedisTemplate.opsForValue()
                .set("email:"+email, refreshToken, JWT_REFRESH_TOKEN_VALIDITY * 1000, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String email) {
        return (String) tokenRedisTemplate.opsForValue()
                .get("email:"+email);
    }

}
