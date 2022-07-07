package com.cgkim.simpleboard.exception;


import com.cgkim.simpleboard.exception.errorcode.ErrorCode;

/**
 * 존재하지 않는 댓글
 */
public class CommentNotFoundException extends BusinessException {

    /**
     * 에러코드 주입
     *
     * @param errorCode
     */
    public CommentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
