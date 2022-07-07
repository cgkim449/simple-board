package com.cgkim.simpleboard.vo.board;

import com.cgkim.simpleboard.domain.board.Board;
import com.cgkim.simpleboard.domain.category.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 게시물 목록 응답
 */
@Getter
public class BoardListResponse {

    private final Long boardId;

    private final String title;

    private final Long categoryId;

    private final String guestNickname;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime registerDate;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime updateDate;

    @Builder
    public BoardListResponse(Long boardId,
                             Long categoryId,
                             String title,
                             String guestNickname,
                             LocalDateTime registerDate,
                             LocalDateTime updateDate
    ) {

        this.boardId = boardId;
        this.categoryId = categoryId;
        this.title = title;
        this.guestNickname = guestNickname;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }

    public static BoardListResponse from(Board board) {
        return BoardListResponse.builder()
                .boardId(board.getBoardId())
                .categoryId(board.getCategory().getCategoryId())
                .title(board.getTitle())
                .guestNickname(board.getGuestNickname())
                .registerDate(board.getRegisterDate())
                .updateDate(board.getUpdateDate())
                .build();
    }
}
