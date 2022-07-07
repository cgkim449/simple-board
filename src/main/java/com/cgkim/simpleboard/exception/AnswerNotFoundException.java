package com.cgkim.simpleboard.exception;


import com.cgkim.simpleboard.exception.errorcode.ErrorCode;

/**
 * 존재하지 않는 답변
 */
public class AnswerNotFoundException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public AnswerNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
