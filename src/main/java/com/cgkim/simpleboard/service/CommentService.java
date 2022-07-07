package com.cgkim.simpleboard.service;

import com.cgkim.simpleboard.domain.board.Board;
import com.cgkim.simpleboard.domain.board.BoardRepository;
import com.cgkim.simpleboard.domain.comment.Comment;
import com.cgkim.simpleboard.domain.comment.CommentRepository;
import com.cgkim.simpleboard.exception.BoardInsertFailedException;
import com.cgkim.simpleboard.exception.BoardNotFoundException;
import com.cgkim.simpleboard.exception.CommentNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.vo.attach.AttachVo;
import com.cgkim.simpleboard.vo.board.BoardDetailResponse;
import com.cgkim.simpleboard.vo.board.BoardListResponse;
import com.cgkim.simpleboard.vo.board.BoardSaveRequest;
import com.cgkim.simpleboard.vo.board.BoardUpdateRequest;
import com.cgkim.simpleboard.vo.comment.CommentListResponse;
import com.cgkim.simpleboard.vo.comment.CommentSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


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