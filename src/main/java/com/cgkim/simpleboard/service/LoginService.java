package com.cgkim.simpleboard.service;

import com.cgkim.simpleboard.domain.Member;
import com.cgkim.simpleboard.dto.member.LoginResponse;
import com.cgkim.simpleboard.exception.LoginFailedException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

/**
 * 로그인 Service
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * 로그인
     *
     * @param username
     * @param password
     * @return MemberVo
     * @throws NoSuchAlgorithmException
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(String username, String password) throws NoSuchAlgorithmException {

        Member member = memberRepository.findByUsername(username);

        if (member == null || member.isPasswordMismatch(password)) {
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        return LoginResponse.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();
    }
}
