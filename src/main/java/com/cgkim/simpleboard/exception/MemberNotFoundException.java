package com.cgkim.simpleboard.exception;


import com.cgkim.simpleboard.exception.errorcode.ErrorCode;

/**
 * 존재하지 않는 회원
 */
public class MemberNotFoundException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
