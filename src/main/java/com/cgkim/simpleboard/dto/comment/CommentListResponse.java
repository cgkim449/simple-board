package com.cgkim.simpleboard.dto.comment;

import com.cgkim.simpleboard.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 댓글 목록 응답
 */
@Getter
public class CommentListResponse {

    private final Long commentId;

    private final Long boardId;

    private final String content;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private final Date registerDate;

    private final String guestNickname;

    private final String memberNickname;

    private final String adminNickname;

    @Builder
    public CommentListResponse(Long commentId, Long boardId, String content, Date registerDate, String guestNickname, String memberNickname, String adminNickname) {
        this.commentId = commentId;
        this.boardId = boardId;
        this.content = content;
        this.registerDate = registerDate;
        this.guestNickname = guestNickname;
        this.memberNickname = memberNickname;
        this.adminNickname = adminNickname;
    }

    public static CommentListResponse from(Comment comment) {

        return CommentListResponse.builder()
                .commentId(comment.getCommentId())
                .boardId(comment.getBoard().getBoardId())
                .content(comment.getContent())
                .guestNickname(comment.getGuestNickname())
                .memberNickname(comment.getMember() != null ? comment.getMember().getNickname() : null)
                .adminNickname(comment.getAdmin() != null ? comment.getAdmin().getNickname() : null)
                .registerDate(comment.getRegisterDate())
                .build();
    }
}
