package com.cgkim.simpleboard.vo.comment;

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

    @Builder
    public CommentSaveRequest(Long boardId, String content, String guestNickname) {

        this.boardId = boardId;
        this.content = content;
        this.guestNickname = guestNickname;
    }

    public Comment toComment() {

        return Comment.builder()
                .content(content)
                .guestNickname(guestNickname)
                .build();
    }
}
