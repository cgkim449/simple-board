package com.cgkim.simpleboard.service;

import com.cgkim.simpleboard.domain.Comment;
import com.cgkim.simpleboard.repository.CommentRepository;
import com.cgkim.simpleboard.exception.CommentNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.dto.comment.CommentSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * 익명 댓글 작성
     *
     * @param commentSaveRequest
     * @return
     */
    public Long writeComment(CommentSaveRequest commentSaveRequest) {
        Long savedCommentId = commentRepository.save(commentSaveRequest.toComment()).getCommentId();
        return savedCommentId;
    }

    /**
     * 댓글 삭제
     *
     * @param commentId
     */
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        commentRepository.deleteById(commentId);
    }


//    public List<CommentListResponse> getCommmentList(Long boardId) {
//        commentRepository.;
//    }
}