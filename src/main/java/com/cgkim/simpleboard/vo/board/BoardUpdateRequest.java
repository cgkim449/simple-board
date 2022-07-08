package com.cgkim.simpleboard.vo.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
public class BoardUpdateRequest {

    private final String title;
    private final String content;

    @Builder
    public BoardUpdateRequest(String title, String content) {

        this.title = title;
        this.content = content;
    }
}
