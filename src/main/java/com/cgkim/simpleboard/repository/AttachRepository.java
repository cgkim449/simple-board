package com.cgkim.simpleboard.repository;

import com.cgkim.simpleboard.domain.Attach;
import com.cgkim.simpleboard.exception.AttachNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class AttachRepository {

    private final EntityManager entityManager;

    public void save(Attach attach) {
        entityManager.persist(attach);
    }

    public Attach findByAttachId(Long attachId) {
        Attach attach = entityManager.find(Attach.class, attachId);

        if (attach == null) {
            throw new AttachNotFoundException(ErrorCode.ATTACH_NOT_FOUND);
        }

        return attach;
    }
}