package com.cgkim.simpleboard.vo.board;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 게시물 검색 요청
 */
@Getter
public class BoardSearchRequest {

    private Integer page; // 페이지

    private String keyword; // 검색어

    private Long categoryId; // 카테고리

    private Date fromDate; // 검색 시작 날짜

    private Date toDate;  // 검색 끝 날짜

    /**
     * 주입
     *
     * @param page
     * @param keyword
     * @param categoryId
     * @param fromDate
     * @param toDate
     */
    public BoardSearchRequest(Integer page, String keyword, Long categoryId, Date fromDate, Date toDate) {

        this.page = (page == null || page < 1) ? 1 : page;
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    private final int limit = 12; // 한 페이지에 표시할 게시물 수

    /**
     * 페이지 offset
     *
     * @return int
     */
    public int getOffset() {
        return (page - 1) * limit;
    }
}
