package com.cgkim.simpleboard.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 댓글 목록 응답
 */
@Getter
@Setter
public class CommentListResponse {

    private Long commentId;

    private Long boardId;

    private String content;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private Date registerDate;

    private String guestNickname;

    private String memberNickname;

    private String adminNickname;
}
