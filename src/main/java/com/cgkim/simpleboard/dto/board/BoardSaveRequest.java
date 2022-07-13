package com.cgkim.simpleboard.dto.board;

import com.cgkim.simpleboard.domain.Board;
import com.cgkim.simpleboard.domain.Category;
import com.cgkim.simpleboard.dto.attach.AttachDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 게시물 작성 요청
 */
@Getter
public class BoardSaveRequest {

    private Long categoryId;

    private  String title;

    private  String content;

    private  String guestNickname;

    private  String guestPassword;

    private  String guestPasswordConfirm;

    private BoardSaveRequest() {
    }

    @Builder
    public BoardSaveRequest(Long categoryId, String title, String content, String guestNickname, String guestPassword, String guestPasswordConfirm) {

        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
        this.guestNickname = guestNickname;
        this.guestPassword = guestPassword;
        this.guestPasswordConfirm = guestPasswordConfirm;
    }
}
