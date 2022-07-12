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

    private final Long categoryId;

    private final String title;

    private final String content;

    private final String guestNickname;

    private final String guestPassword;

    private final String guestPasswordConfirm;

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