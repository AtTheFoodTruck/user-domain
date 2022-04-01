package com.sesac.foodtruckuser.application.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider implements Serializable, InitializingBean {
    private static final long serialVersionUID = -2550185165626007488L;

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private static final long JWT_ACCESS_TOKEN_VALIDITY = 60 * 30; // 30 minutes
    private static final long JWT_REFRESH_TOKEN_VALIDITY = 60 * 60 * 24 * 7; // 1 week

    private Key key;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret
    ) {
        this.secret = secret;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    /**
     * token 의 모든 claim 반환
     * @param token: jwt token
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
     **/
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * token 으로부터 username, role map 반환
     * @param token: jwt access token
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    public Map<String, Object> getUserParseInfo(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Map<String, Object> result = new HashMap<>();
        result.put("email", claims.getSubject()); // expected : getSubject("email"),
        result.put(AUTHORITIES_KEY, claims.get(AUTHORITIES_KEY));

        log.info("권한이 담긴 result  = {}", result);

        return result;
    }

    /**
     * access token 에 담겨있는 권한 정보들(claims)을 이용해 Authentication 객체 리턴
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    public Authentication getAuthentication(String token) {
        Claims claims = getAllClaimsFromToken(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * AccessToken 생성
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-27
    **/
    // Authentication 객체의 권한 정보를 이용해서 토큰을 생성
    public String createToken(Authentication authentication, boolean isRefreshToken) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date validity = null;
        if (isRefreshToken) {
            validity = new Date(now + JWT_ACCESS_TOKEN_VALIDITY * 1000);
        } else {
            validity = new Date(now + JWT_REFRESH_TOKEN_VALIDITY * 1000);
        }

        return Jwts.builder()
                .setSubject(authentication.getName())  // email
                .claim(AUTHORITIES_KEY, authorities)  // authorities
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }
    /**
     * Jwt token 유효성 여부
     * @author jjaen
     * @modifier jaemin, validation 메서드 수정
     * @version 1.0.0
     * 작성일 2022/03/27
     * 수정일 2022/03/28
     **/
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * 토큰 유효성, 만료시간 체크
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-31
     **/
    public boolean validateExpiration(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (ExpiredJwtException e) {
            log.info(e.getMessage());
            return false;
        }
    }

    /**
     * AccessToken의 남은 유효시간 반환
     *
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();

        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
