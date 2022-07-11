package com.cgkim.simpleboard.service;

import com.cgkim.simpleboard.domain.Member;
import com.cgkim.simpleboard.exception.MemberNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 회원 Service
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * username 으로 memberId 조회
     *
     * @param username
     * @return Long
     */
    public void getMemberByUsername(String username) {

        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
