package com.cgkim.simpleboard.service;

import com.cgkim.simpleboard.domain.Board;
import com.cgkim.simpleboard.domain.Comment;
import com.cgkim.simpleboard.domain.Member;
import com.cgkim.simpleboard.dto.board.BoardListResponse;
import com.cgkim.simpleboard.dto.comment.CommentListResponse;
import com.cgkim.simpleboard.dto.common.GuestPasswordCheckRequest;
import com.cgkim.simpleboard.exception.GuestPasswordInvalidException;
import com.cgkim.simpleboard.exception.GuestPasswordMismatchException;
import com.cgkim.simpleboard.exception.LoginRequiredException;
import com.cgkim.simpleboard.exception.NoAuthorizationException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.repository.BoardRepository;
import com.cgkim.simpleboard.dto.comment.CommentSaveRequest;
import com.cgkim.simpleboard.repository.CommentRepository;
import com.cgkim.simpleboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    /**
     * 회원 댓글 작성
     *
     * @param username
     * @param commentSaveRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long writeComment(String username, CommentSaveRequest commentSaveRequest) throws NoSuchAlgorithmException {

        //Member 엔티티 조회
        Member member = memberRepository.findByUsername(username);

        //Board 엔티티 조회
        Board board = boardRepository.findById(commentSaveRequest.getBoardId());

        //Comment 엔티티 생성
        Comment comment = Comment.createComment(
                board,
                member,
                commentSaveRequest.getContent()
        );

        commentRepository.save(comment);
        return comment.getCommentId();
    }

    /**
     * 익명 댓글 작성
     *
     * @param commentSaveRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long writeComment(CommentSaveRequest commentSaveRequest) throws NoSuchAlgorithmException {

        //Board 엔티티 조회
        Board board = boardRepository.findById(commentSaveRequest.getBoardId());

        //Comment 엔티티 생성
        Comment comment = Comment.createComment(
                board,
                commentSaveRequest.getContent(),
                commentSaveRequest.getGuestNickname(),
                commentSaveRequest.getGuestPassword()
        );

        commentRepository.save(comment);
        return comment.getCommentId();
    }


    /**
     * 댓글 삭제
     *
     * @param commentId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId) {

        commentRepository.deleteByCommentId(commentId);
    }

    /**
     * 댓글 목록 조회
     *
     * @param boardId
     * @return
     */
    public List<CommentListResponse> getCommentList(Long boardId) {
        List<Comment> commentList = commentRepository.findAllByBoardId(boardId);

        return getCommentListResponsesFrom(commentList);
    }

    private List<CommentListResponse> getCommentListResponsesFrom(List<Comment> commentList) {

        List<CommentListResponse> commentListResponses = new ArrayList<>();

        for (Comment comment : commentList) {
            commentListResponses.add(CommentListResponse.from(comment));
        }

        return commentListResponses;
    }

    public void checkOwner(Long commentId, String username, GuestPasswordCheckRequest guestPasswordCheckRequest) throws NoSuchAlgorithmException {
        Comment comment = commentRepository.findByCommentId(commentId);

        if (comment.getAdmin() != null) { //관리자 글이면
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);

        } else if (comment.getGuestNickname() != null) { //익명 글이면

            validateGuestPassword(guestPasswordCheckRequest); //비밀번호 유효성 검증
            checkGuestPassword(commentId, guestPasswordCheckRequest); //비밀번호 체크

        } else if (comment.getMember() != null) { //회원 글이면

            if (username == null) {
                throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
            }

            Member member = memberRepository.findByUsername(username);

            if (member == null) {
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }

            //TODO: 동일 비교 공부 memberId 가 같으면 같은 객체이다
            if (comment.getMember() != member) { //검증
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }
        }
    }

    private void validateGuestPassword(GuestPasswordCheckRequest guestPasswordCheckRequest) {

        String guestPassword = guestPasswordCheckRequest.getGuestPassword();

        if (guestPassword == null || guestPassword.equals("")) {
            throw new GuestPasswordInvalidException(ErrorCode.GUEST_PASSWORD_INVALID);
        }
    }

    /**
     * 익명 게시물 비밀번호 체크
     *
     * @param commentId
     * @param guestPasswordCheckRequest
     * @throws NoSuchAlgorithmException
     */
    public void checkGuestPassword(Long commentId, GuestPasswordCheckRequest guestPasswordCheckRequest) throws NoSuchAlgorithmException {

        String guestPassword = guestPasswordCheckRequest.getGuestPassword();

        Comment comment = commentRepository.findByCommentId(commentId);

        if (comment.isPasswordMismatch(guestPassword)) {
            throw new GuestPasswordMismatchException(ErrorCode.GUEST_PASSWORD_MISMATCH);
        }
    }
}