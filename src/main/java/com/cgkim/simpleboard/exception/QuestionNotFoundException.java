package com.cgkim.simpleboard.exception;


import com.cgkim.simpleboard.exception.errorcode.ErrorCode;

/**
 * 존재하지 않는 질문
 */
public class QuestionNotFoundException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public QuestionNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
