package com.cgkim.simpleboard.exception;


import com.cgkim.simpleboard.exception.errorcode.ErrorCode;

/**
 * 권한 없음
 */
public class NoAuthorizationException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public NoAuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
