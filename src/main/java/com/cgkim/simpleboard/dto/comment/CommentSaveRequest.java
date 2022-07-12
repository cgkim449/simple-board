package com.cgkim.simpleboard.dto.comment;

import com.cgkim.simpleboard.domain.Comment;
import lombok.Builder;
import lombok.Getter;

/**
 * 댓글 등록 요청
 */
@Getter
public class CommentSaveRequest {

    private final Long boardId;

    private final String content;

    private final String guestNickname;

    private final String guestPassword;

    private final String guestPasswordConfirm;

    @Builder
    public CommentSaveRequest(Long boardId,
                              String content,
                              String guestNickname,
                              String guestPassword,
                              String guestPasswordConfirm
    ) {

        this.boardId = boardId;
        this.content = content;
        this.guestNickname = guestNickname;
        this.guestPassword = guestPassword;
        this.guestPasswordConfirm = guestPasswordConfirm;
    }

    public Comment toComment() {

        return Comment.builder()
                .content(content)
                .guestNickname(guestNickname)
                .build();
    }
}
