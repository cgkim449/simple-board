package com.cgkim.simpleboard.exception;


import com.cgkim.simpleboard.exception.errorcode.ErrorCode;

/**
 * 존재하지 않는 게시물
 */
public class BoardNotFoundException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public BoardNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
