package com.cgkim.simpleboard.repository;

import com.cgkim.simpleboard.domain.Attach;
import com.cgkim.simpleboard.domain.Board;
import com.cgkim.simpleboard.dto.attach.AttachDto;
import com.cgkim.simpleboard.exception.BoardNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.dto.board.BoardSearchRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cgkim.simpleboard.domain.QAdmin.admin;
import static com.cgkim.simpleboard.domain.QBoard.board;
import static com.cgkim.simpleboard.domain.QCategory.category;
import static com.cgkim.simpleboard.domain.QMember.member;

@RequiredArgsConstructor
@Repository
public class BoardRepository {

    private final EntityManager em;

    /**
     * 게시물 저장
     *
     * @param board
     */
    public void save(Board board) {

        if (board.getBoardId() == null) {
            em.persist(board);
        } else {
            em.merge(board);
        }
    }

    /**
     * boardId 로 게시물 조회
     *
     * @param boardId
     * @return
     */
    public Board findById(Long boardId) {
        //TODO: query 싱글톤?
        JPAQueryFactory query = new JPAQueryFactory(em);

        Board fetchedBoard = query.select(board)
                .from(board)
                .leftJoin(board.member, member)
                .leftJoin(board.admin, admin)
                .where(boardIdEq(boardId))
                .fetchOne();

        if(fetchedBoard == null) {
            throw new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

        return fetchedBoard;
    }

    private BooleanExpression boardIdEq(Long boardId) {

        if (boardId == null || boardId == 0) {
            return null;
        }

        return board.boardId.eq(boardId);
    }

    /**
     * boardId 로 게시물 삭제
     *
     * @param boardId
     * @return
     */
    //TODO: 왜 삭제 한번에 다되는지 정리
    public List<AttachDto> deleteByBoardId(Long boardId) {

        Board board = findById(boardId);

        if (board == null) {
            throw new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

        List<Attach> attaches = board.getAttaches();
        List<AttachDto> attachDTOs = new ArrayList<>();
        for (Attach attach : attaches) {
            attachDTOs.add(AttachDto.builder()
                    .attachId(attach.getAttachId())
                    .boardId(attach.getBoard().getBoardId())
                    .name(attach.getName())
                    .uploadPath(attach.getUploadPath())
                    .extension(attach.getExtension())
                    .isImage(attach.getIsImage())
                    .uuid(attach.getUuid())
                    .size(attach.getSize())
                    .build());
        }

        em.remove(board);

        return attachDTOs;
    }

    /**
     * 검색 조건에 맞는 게시물 목록 조회
     *
     * @param boardSearchRequest
     * @return
     */
    public List<Board> findAll(BoardSearchRequest boardSearchRequest) {

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

    /**
     * 검색조건 동적 쿼리
     *
     * @param toDate
     * @return
     */
    private BooleanExpression toDateBefore(Date toDate) {

        if(toDate == null) {
            return null;
        }

        return board.registerDate.before(toDate);
    }

    private BooleanExpression fromDateAfter(Date fromDate) {

        if(fromDate == null) {
            return null;
        }

        return board.registerDate.after(fromDate);
    }

    private BooleanExpression keywordLike(String keyword) {

        if (keyword == null) {
            return null;
        }

        return board.title.like("%" + keyword + "%")
                .or(board.content.like("%" + keyword + "%"))
                .or(board.guestNickname.like("%" + keyword + "%"));
    }

    private BooleanExpression categoryEq(Long categoryId) {

        if (categoryId == null || categoryId == 0) {
            return null;
        }

        return board.category.categoryId.eq(categoryId);
    }

    /**
     * 검색 조건에 맞는 게시물 개수 리턴
     *
     * @param boardSearchRequest
     * @return
     */
    public Long count(BoardSearchRequest boardSearchRequest) {

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(board.count())
                .from(board)
                .join(board.category, category)
                .where(categoryEq(boardSearchRequest.getCategoryId()),
                        keywordLike(boardSearchRequest.getKeyword()),
                        fromDateAfter(boardSearchRequest.getFromDate()),
                        toDateBefore(boardSearchRequest.getToDate())
                )
                .fetchOne();
    }
}