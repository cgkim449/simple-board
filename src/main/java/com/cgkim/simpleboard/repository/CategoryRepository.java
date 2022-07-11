package com.cgkim.simpleboard.repository;

import com.cgkim.simpleboard.domain.Board;
import com.cgkim.simpleboard.domain.Category;
import com.cgkim.simpleboard.domain.QBoard;
import com.cgkim.simpleboard.domain.QCategory;
import com.cgkim.simpleboard.dto.board.BoardSearchRequest;
import com.cgkim.simpleboard.exception.BoardNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CategoryRepository {

    private final EntityManager em;

    /**
     * categoryId 로 게시물 조회
     *
     * @param categoryId
     * @return
     */
    public Category findByCategoryId(Long categoryId) {
        //TODO: category not found
        return em.find(Category.class, categoryId);
    }


    /**
     * 검색 조건에 맞는 게시물 목록 조회
     *
     * @param boardSearchRequest
     * @return
     */
    public List<Board> findAll(BoardSearchRequest boardSearchRequest) {

        QBoard board = QBoard.board;
        QCategory category = QCategory.category;

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(board)
                .from(board)
                .join(board.category, category)
                .where(categoryEq(boardSearchRequest.getCategoryId()),
                        keywordLike(boardSearchRequest.getKeyword()),
                        fromDateAfter(boardSearchRequest.getFromDate()),
                        toDateBefore(boardSearchRequest.getToDate())
                )
                .offset(boardSearchRequest.getOffset())
                .limit(boardSearchRequest.getLimit())
                .orderBy(board.registerDate.desc(), board.boardId.desc())
                .fetch();
    }

    private BooleanExpression toDateBefore(Date toDate) {

        if(toDate == null) {
            return null;
        }

        return QBoard.board.registerDate.before(toDate);
    }

    private BooleanExpression fromDateAfter(Date fromDate) {

        if(fromDate == null) {
            return null;
        }

        return QBoard.board.registerDate.after(fromDate);
    }

    private BooleanExpression keywordLike(String keyword) {

        if (keyword == null) {
            return null;
        }

        return QBoard.board.title.like("%" + keyword + "%")
                .or(QBoard.board.content.like("%" + keyword + "%"))
                .or(QBoard.board.guestNickname.like("%" + keyword + "%"));
    }

    private BooleanExpression categoryEq(Long categoryId) {

        if (categoryId == null || categoryId == 0) {
            return null;
        }

        return QBoard.board.category.categoryId.eq(categoryId);
    }

    //TODO: select count 로 바꾸기
    /**
     * 검색 조건에 맞는 게시물 개수 리턴
     *
     * @param boardSearchRequest
     * @return
     */
    public long count(BoardSearchRequest boardSearchRequest) {

        QBoard board = QBoard.board;
        QCategory category = QCategory.category;

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(board)
                .from(board)
                .join(board.category, category)
                .where(categoryEq(boardSearchRequest.getCategoryId()),
                        keywordLike(boardSearchRequest.getKeyword()),
                        fromDateAfter(boardSearchRequest.getFromDate()),
                        toDateBefore(boardSearchRequest.getToDate())
                )
                .offset(boardSearchRequest.getOffset())
                .limit(boardSearchRequest.getLimit())
                .orderBy(board.registerDate.desc(), board.boardId.desc())
                .fetch()
                .size();
    }
}