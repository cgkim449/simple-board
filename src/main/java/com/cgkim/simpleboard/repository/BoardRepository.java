package com.cgkim.simpleboard.repository;

import com.cgkim.simpleboard.domain.Board;
import com.cgkim.simpleboard.domain.QBoard;
import com.cgkim.simpleboard.domain.QCategory;
import com.cgkim.simpleboard.exception.BoardNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.vo.board.BoardSearchRequest;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {

    private final EntityManager em;

    public void save(Board board) {
        if (board.getBoardId() == null) {
            em.persist(board);
        } else {
            em.merge(board);
        }
    }

    public Board findById(Long boardId) {
        return em.find(Board.class, boardId);
    }

    public void deleteById(Long boardId) {
        Board board = findById(boardId);

        if (board == null) {
            throw new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

        em.remove(board);
    }

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

    //TODO: count
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
                .fetch()
                .size();
    }
}