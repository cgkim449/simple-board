package com.cgkim.simpleboard.controller;

import com.cgkim.simpleboard.dto.member.LoginRequest;
import com.cgkim.simpleboard.dto.member.LoginResponse;
import com.cgkim.simpleboard.jwt.JwtProvider;
import com.cgkim.simpleboard.response.SuccessResponse;
import com.cgkim.simpleboard.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

/**
 * 로그인 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class LoginController {

    private final LoginService loginService;

    private final JwtProvider jwtProvider;

    /**
     * 로그인
     *
     * @param loginRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws NoSuchAlgorithmException {

        LoginResponse loginResponse = loginService.login(loginRequest.getUsername(), loginRequest.getPassword()); // 로그인

        String token = jwtProvider.createToken(loginResponse.getUsername()); // 토큰 생성

        return ResponseEntity.ok(
                new SuccessResponse()
                        .put("username", loginResponse.getUsername())
                        .put("nickname", loginResponse.getNickname())
                        .put("token", token));
    }
}
