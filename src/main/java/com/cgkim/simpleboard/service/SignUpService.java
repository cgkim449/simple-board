package com.cgkim.simpleboard.service;

import com.cgkim.simpleboard.domain.Member;
import com.cgkim.simpleboard.dto.member.SignUpRequest;
import com.cgkim.simpleboard.exception.NicknameDuplicateException;
import com.cgkim.simpleboard.exception.UsernameDuplicateException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.repository.BoardRepository;
import com.cgkim.simpleboard.repository.MemberRepository;
import com.cgkim.simpleboard.util.SHA256PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

/**
 * 회원가입 Service
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SignUpService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     *
     * @param signUpRequest
     * @return String
     * @throws NoSuchAlgorithmException
     */
    @Transactional(rollbackFor = Exception.class)
    public String signUp(SignUpRequest signUpRequest) throws NoSuchAlgorithmException {

        if (isUsernameDuplicated(signUpRequest.getUsername())) {
            throw new UsernameDuplicateException(ErrorCode.USERNAME_DUPLICATE);
        }

        if (isNicknameDuplicated(signUpRequest.getNickname())) {
            throw new NicknameDuplicateException(ErrorCode.NICKNAME_DUPLICATE);
        }

        Member member = Member.createMember(
                signUpRequest.getUsername(),
                signUpRequest.getNickname(),
                signUpRequest.getPassword()
        );

        memberRepository.save(member);

        return member.getUsername();
    }

    private boolean isNicknameDuplicated(String nickname) {

        return memberRepository.countByNickname(nickname) > 0;
    }

    private boolean isUsernameDuplicated(String username) {

        return memberRepository.countByUsername(username) > 0;
    }
}
