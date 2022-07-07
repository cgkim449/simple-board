package com.cgkim.simpleboard.vo.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 익명 글/댓글 비밀번호 검증 요청
 */
@Getter
@Setter
public class GuestPasswordCheckRequest {

    private String guestPassword;
}
