package com.cgkim.simpleboard.repository;

import com.cgkim.simpleboard.domain.Comment;
import com.cgkim.simpleboard.exception.CommentNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.cgkim.simpleboard.domain.QAdmin.admin;
import static com.cgkim.simpleboard.domain.QComment.comment;
import static com.cgkim.simpleboard.domain.QMember.member;

@Repository
public class CommentRepository {

    private final EntityManager em;

    private final JPAQueryFactory jpaQueryFactory;

    public CommentRepository(EntityManager em) {
        this.em = em;
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }
    /**
     * 댓글 저장
     *
     * @param comment
     */
    public void save(Comment comment) {

        if (comment.getCommentId() == null) {
            em.persist(comment);
        } else {
            em.merge(comment);
        }
    }

    /**
     * commentId 로 게시물 조회
     *
     * @param commentId
     * @return
     */
    public Comment findByCommentId(Long commentId) {

        Comment fetchedComment = jpaQueryFactory.select(comment)
                .from(comment)
                .where(commentIdEq(commentId))
                .fetchOne();

        if(fetchedComment == null) {
            throw new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return fetchedComment;
    }

    private BooleanExpression commentIdEq(Long commentId) {

        if (commentId == null || commentId == 0) {
            return null;
        }

        return comment.commentId.eq(commentId);
    }

    public Comment deleteByCommentId(Long commentId) {
        Comment comment = findByCommentId(commentId);

        if (comment == null) {
            throw new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }

        em.remove(comment);
        return comment;
    }

    public List<Comment> findAllByBoardId(Long boardId) {

        return jpaQueryFactory.select(comment)
                .from(comment)
                .leftJoin(comment.member, member)
                .leftJoin(comment.admin, admin)
                .where(boardIdEq(boardId))
                .orderBy(comment.registerDate.asc(), comment.commentId.desc())
                .fetch();
    }

    private BooleanExpression boardIdEq(Long boardId) {

        if (boardId == null || boardId == 0) {
            return null;
        }

        return comment.board.boardId.eq(boardId);
    }
}