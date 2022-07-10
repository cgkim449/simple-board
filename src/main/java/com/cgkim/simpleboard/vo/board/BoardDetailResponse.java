package com.cgkim.simpleboard.vo.board;

import com.cgkim.simpleboard.domain.Attach;
import com.cgkim.simpleboard.domain.Board;
import com.cgkim.simpleboard.util.AttachURIProvider;
import com.cgkim.simpleboard.vo.attach.AttachVo;
import com.cgkim.simpleboard.vo.comment.CommentListResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 게시물 상세 조회 응답
 */
@Getter @Setter
public class BoardDetailResponse {

    private Long boardId;

    private String title;

    private String content;

    private int viewCount;

    private boolean hasAttach;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private Long categoryId;

    private String categoryName;

    private String guestNickname;

    private String memberUsername;

    private String memberNickname;

    private String adminUsername;

    private String adminNickname;

    private List<CommentListResponse> commentList;

    private List<AttachVo> attachList;

    @Builder
    public BoardDetailResponse(Long boardId, String title, String content, int viewCount, boolean hasAttach, Date registerDate, Date updateDate, Long categoryId, String categoryName, String guestNickname, String memberUsername, String memberNickname, String adminUsername, String adminNickname, List<CommentListResponse> commentList, List<AttachVo> attachList) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.hasAttach = hasAttach;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.guestNickname = guestNickname;
        this.memberUsername = memberUsername;
        this.memberNickname = memberNickname;
        this.adminUsername = adminUsername;
        this.adminNickname = adminNickname;
        this.commentList = commentList;
        this.attachList = attachList;
    }

    public static BoardDetailResponse from(Board board) {

        return BoardDetailResponse.builder()
                .boardId(board.getBoardId())
                .categoryId(board.getCategory().getCategoryId())
                .categoryName(board.getCategory().getName())
                .hasAttach(board.getHasAttach() == null || board.getHasAttach() == 0 ? false : true)
                .title(board.getTitle())
                .content(board.getContent())
                .guestNickname(board.getGuestNickname())
                .registerDate(board.getRegisterDate())
                .updateDate(board.getUpdateDate())
                .build();
    }
}
