package com.cgkim.simpleboard.repository;

import com.cgkim.simpleboard.domain.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.cgkim.simpleboard.domain.QMember.member;


@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {

        if (member.getMemberId() == null) {
            em.persist(member);
        } else {
            em.merge(member);
        }
    }

    public Long countByNickname(String nickname) {

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(member.count())
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

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(member.count())
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

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(member)
                .from(member)
                .where(usernameEq(username))
                .fetchOne();
    }
}