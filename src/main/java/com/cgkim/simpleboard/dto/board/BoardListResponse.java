package com.cgkim.simpleboard.dto.board;

import com.cgkim.simpleboard.domain.Board;
import com.cgkim.simpleboard.util.AttachURIProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.Tuple;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

/**
 * 게시물 목록 응답
 */
@Getter
public class BoardListResponse {

    private final Long boardId;

    private final Long commentCount;

    private final String title;

    private final int viewCount;

    private final Boolean hasAttach;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private final Date registerDate;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private final Date updateDate;

    private final Long categoryId;

    private final String categoryName;

    private final String guestNickname;

    private final String memberNickname;

    private final String adminNickname;

    private final String thumbnailUri;

    @Builder
    public BoardListResponse(Long boardId, Long commentCount, String title, int viewCount, Boolean hasAttach, Date registerDate, Date updateDate, Long categoryId, String categoryName, String guestNickname, String memberNickname, String adminNickname, String thumbnailUri) {
        this.boardId = boardId;
        this.commentCount = commentCount;
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

    public static BoardListResponse from(Board board, Long commentCount) {

        return BoardListResponse.builder()
                .commentCount(commentCount)
                .boardId(board.getBoardId())
//                .categoryId(board.getCategory().getCategoryId())
//                .categoryName(board.getCategory().getName())
                .hasAttach(board.getHasAttach())
                .title(board.getTitle())
                .guestNickname(board.getGuestNickname())
                .memberNickname(board.getMember() != null ? board.getMember().getNickname() : null)
                .adminNickname(board.getAdmin() != null ? board.getAdmin().getNickname() : null)
                .viewCount(board.getViewCount())
                .thumbnailUri(AttachURIProvider.getFullURIOf(board.getThumbnailUri()))
                .registerDate(board.getRegisterDate())
                .updateDate(board.getUpdateDate())
                .build();
    }
}
