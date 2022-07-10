package com.cgkim.simpleboard.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 토큰 생성 및 검증
 */
@Slf4j
@Component
public class JwtProvider {

    private final String secretKey;

    private final long maxAge;

    /**
     * secret key, 유효기간 설정 주입
     *
     * @param secretKey
     * @param maxAge
     */
    public JwtProvider(@Value("${jwt.secret-key}") String secretKey,
                       @Value("${jwt.token-validity}") long maxAge) {

        this.secretKey = secretKey;
        this.maxAge = maxAge;
    }

    /**
     * 토큰 생성
     *
     * @param username
     * @return
     */
    public String createToken(String username) {

        Date now = new Date();
        Date expires = new Date(now.getTime() + maxAge);

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withClaim("username", username)
                .withIssuedAt(now)
                .withExpiresAt(expires)
                .sign(algorithm);
    }

    /**
     * 토큰 검증
     *
     * @param token
     * @return DecodedJWT
     */
    public DecodedJWT validate(String token) {

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();

        return verifier.verify(token);
    }


}
