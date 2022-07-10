package com.cgkim.simpleboard.vo.board;

import com.cgkim.simpleboard.domain.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 게시물 목록 응답
 */
@Getter
public class BoardListResponse {

    private Long boardId;

    private String title;

    private int viewCount;

    private Integer hasAttach;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private Long categoryId;

    private String categoryName;

    private String guestNickname;

    private String memberNickname;

    private String adminNickname;

    private String thumbnailUri;

    @Builder
    public BoardListResponse(Long boardId, String title, int viewCount, int hasAttach, Date registerDate, Date updateDate, Long categoryId, String categoryName, String guestNickname, String memberNickname, String adminNickname, String thumbnailUri) {
        this.boardId = boardId;
        this.title = title;
        this.viewCount = viewCount;
        this.hasAttach = hasAttach;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.guestNickname = guestNickname;
        this.memberNickname = memberNickname;
        this.adminNickname = adminNickname;
        this.thumbnailUri = thumbnailUri;
    }

    public static BoardListResponse from(Board board) {

        return BoardListResponse.builder()
                .boardId(board.getBoardId())
                .categoryId(board.getCategory().getCategoryId())
                .categoryName(board.getCategory().getName())
                .hasAttach(board.getHasAttach() == null || board.getHasAttach() == 0 ? 0 : 1)
                .title(board.getTitle())
                .guestNickname(board.getGuestNickname())
                .registerDate(board.getRegisterDate())
                .updateDate(board.getUpdateDate())
                .build();
    }
}
