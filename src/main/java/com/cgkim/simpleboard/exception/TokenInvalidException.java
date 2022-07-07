package com.cgkim.simpleboard.exception;


import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import lombok.Getter;

/**
 * 유효하지 않은 토큰
 */
@Getter
public class TokenInvalidException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * 에러코드 주입
     * @param errorCode
     */
    public TokenInvalidException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
