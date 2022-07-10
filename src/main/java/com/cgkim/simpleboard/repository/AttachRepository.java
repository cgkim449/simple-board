package com.cgkim.simpleboard.repository;

import com.cgkim.simpleboard.domain.Attach;
import com.cgkim.simpleboard.domain.QAttach;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class AttachRepository {

    private final EntityManager em;

    public void save(Attach attach) {
        em.persist(attach);
    }

    public List<Attach> findAll(Long boardId) {

        QAttach attach = QAttach.attach;

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(attach)
                .from(attach)
                .where(boardEq(boardId))
                .fetch();
    }

    private BooleanExpression boardEq(Long boardId) {
        if (boardId == null || boardId == 0) {
            return null;
        }
        return QAttach.attach.board.boardId.eq(boardId);
    }
}