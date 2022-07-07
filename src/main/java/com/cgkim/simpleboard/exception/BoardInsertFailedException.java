package com.cgkim.simpleboard.exception;


import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.vo.attach.AttachVo;

import java.util.List;

/**
 * 게시물 등록 실패
 */
public class BoardInsertFailedException extends InsertFailedException{

    /**
     * 물리적 파일 생성이 생성된 첨부파일 목록 및 에러코드 주입
     *
     * @param attachSaveList
     * @param errorCode
     */
    public BoardInsertFailedException(List<AttachVo> attachSaveList, ErrorCode errorCode) {
        super(attachSaveList, errorCode);
    }
}
