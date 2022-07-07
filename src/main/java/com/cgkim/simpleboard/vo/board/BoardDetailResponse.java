package com.cgkim.simpleboard.vo.board;

import com.cgkim.simpleboard.domain.board.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 게시물 상세 조회 응답
 */
@Getter
public class BoardDetailResponse {

    private final Long boardId;

    private final Long categoryId;

    private final String title;

    private final String content;

    private final String guestNickname;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime registerDate;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime updateDate;

    @Builder
    public BoardDetailResponse(Long boardId,
                               Long categoryId,
                               String title,
                               String content,
                               String guestNickname,
                               LocalDateTime registerDate,
                               LocalDateTime updateDate
    ) {

        this.boardId = boardId;
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
        this.guestNickname = guestNickname;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }

    public static BoardDetailResponse from(Board board) {
        return BoardDetailResponse.builder()
                .boardId(board.getBoardId())
                .categoryId(board.getCategory().getCategoryId())
                .title(board.getTitle())
                .content(board.getContent())
                .guestNickname(board.getGuestNickname())
                .registerDate(board.getRegisterDate())
                .updateDate(board.getUpdateDate())
                .build();
    }
}
