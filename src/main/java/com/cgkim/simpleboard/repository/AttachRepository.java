package com.cgkim.simpleboard.repository;

import com.cgkim.simpleboard.domain.Attach;
import com.cgkim.simpleboard.domain.Category;
import com.cgkim.simpleboard.domain.QAttach;
import com.cgkim.simpleboard.exception.AttachNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
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

    public Attach findByAttachId(Long attachId) {
        Attach attach = em.find(Attach.class, attachId);

        if (attach == null) {
            throw new AttachNotFoundException(ErrorCode.ATTACH_NOT_FOUND);
        }

        return attach;
    }
}