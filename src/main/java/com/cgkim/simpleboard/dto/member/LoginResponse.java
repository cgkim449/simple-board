package com.cgkim.simpleboard.dto.member;

import lombok.Builder;
import lombok.Getter;

/**
 * 회원 VO
 */
@Getter
public class LoginResponse {

    private final String username;


    private final String nickname;


    @Builder
    public LoginResponse(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
}
