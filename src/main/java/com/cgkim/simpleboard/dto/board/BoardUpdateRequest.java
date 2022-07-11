package com.cgkim.simpleboard.dto.board;

import lombok.Builder;
import lombok.Getter;


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
