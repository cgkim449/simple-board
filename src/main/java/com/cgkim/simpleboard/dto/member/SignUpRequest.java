package com.cgkim.simpleboard.dto.member;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 요청
 */
@Getter
@Setter
public class SignUpRequest {

    private String username;

    private String password;

    private String passwordConfirm;

    private String nickname;
}
