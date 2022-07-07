package com.cgkim.simpleboard.vo.board;

import com.cgkim.simpleboard.domain.board.Board;
import com.cgkim.simpleboard.domain.category.Category;
import lombok.Builder;
import lombok.Getter;

/**
 * 게시물 작성 요청
 */
@Getter
public class BoardSaveRequest {

    private final Category category;

    private final String title;

    private final String content;

    private final String guestNickname;

    @Builder
    public BoardSaveRequest(Category category, String title, String content, String guestNickname) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.guestNickname = guestNickname;
    }

    public Board toBoard() {
        return Board.builder()
                .category(category)
                .title(title)
                .content(content)
                .guestNickname(guestNickname)
                .build();
    }
}
