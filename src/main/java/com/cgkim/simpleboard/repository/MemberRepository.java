package com.cgkim.simpleboard.repository;

import com.cgkim.simpleboard.domain.Member;
import com.cgkim.simpleboard.exception.MemberNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.cgkim.simpleboard.domain.QMember.member;


@Repository
public class MemberRepository {

    private final EntityManager em;

    private final JPAQueryFactory jpaQueryFactory;

    public MemberRepository(EntityManager em) {
        this.em = em;
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public void save(Member member) {

        if (member.getMemberId() == null) {
            em.persist(member);
        } else {
            em.merge(member);
        }
    }

    public Long countByNickname(String nickname) {

        return jpaQueryFactory.select(member.count())
                .from(member)
                .where(nicknameEq(nickname))
                .fetchOne();
    }

    private BooleanExpression nicknameEq(String nickname) {

        if (nickname == null || nickname.equals("")) {
            return null;
        }

        return member.nickname.eq(nickname);
    }

    public Long countByUsername(String username) {

        return jpaQueryFactory.select(member.count())
                .from(member)
                .where(usernameEq(username))
                .fetchOne();
    }

    private BooleanExpression usernameEq(String username) {

        if (username == null || username.equals("")) {
            return null;
        }

        return member.username.eq(username);
    }

    public Member findByUsername(String username) {

        Member fetchedMember = jpaQueryFactory.select(member)
                .from(member)
                .where(usernameEq(username))
                .fetchOne();

        if(fetchedMember == null) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return fetchedMember;
    }
}