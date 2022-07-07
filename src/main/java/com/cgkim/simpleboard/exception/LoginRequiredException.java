package com.cgkim.simpleboard.exception;


import com.cgkim.simpleboard.exception.errorcode.ErrorCode;

/**
 * 로그인 해야만 이용 가능
 */
public class LoginRequiredException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public LoginRequiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
