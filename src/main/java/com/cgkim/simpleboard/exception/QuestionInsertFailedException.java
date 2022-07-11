package com.cgkim.simpleboard.exception;


import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.dto.attach.AttachDto;

import java.util.List;

/**
 * 질문 작성 실패
 */
public class QuestionInsertFailedException extends InsertFailedException{

    /**
     * 물리적 파일이 생성된 첨부파일 목록 및 에러코드 주입
     *
     * @param attachSaveList
     * @param errorCode
     */
    public QuestionInsertFailedException(List<AttachDto> attachSaveList, ErrorCode errorCode) {
        super(attachSaveList, errorCode);
    }
}
