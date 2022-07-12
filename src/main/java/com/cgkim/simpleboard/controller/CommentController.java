package com.cgkim.simpleboard.controller;

import com.cgkim.simpleboard.argumentresolver.LoginUser;
import com.cgkim.simpleboard.dto.comment.CommentListResponse;
import com.cgkim.simpleboard.dto.common.GuestPasswordCheckRequest;
import com.cgkim.simpleboard.exception.LoginRequiredException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.response.SuccessResponse;
import com.cgkim.simpleboard.service.CommentService;
import com.cgkim.simpleboard.dto.comment.CommentSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 자유게시판 댓글 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {

    private final CommentService commentService;


    /**
     * 회원 댓글 작성
     *
     * @param username
     * @param commentSaveRequest
     * @return ResponseEntity<SuccessResponse>
     */
    @PostMapping("/member")
    public ResponseEntity<SuccessResponse> writeMemberComment(@LoginUser String username,
                                                              @Valid CommentSaveRequest commentSaveRequest
    ) throws NoSuchAlgorithmException {

        if (username == null) {
            throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
        }

        Long commentId = commentService.writeComment(username, commentSaveRequest);

        return ResponseEntity.created(URI.create("/comments/" + commentId)).body(new SuccessResponse());
    }

    /**
     * 익명 댓글 작성
     *
     * @param commentSaveRequest
     * @return
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/guest")
    public ResponseEntity<SuccessResponse> writeComment(@Valid CommentSaveRequest commentSaveRequest) throws NoSuchAlgorithmException {

        Long commentId = commentService.writeComment(commentSaveRequest);

        return ResponseEntity.created(URI.create("/comments/" + commentId)).body(new SuccessResponse());
    }

    /**
     * 댓글 목록 조회
     *
     * @param boardId
     * @return ResponseEntity<SuccessResponse>
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getCommentList(Long boardId) {

        List<CommentListResponse> commentList = commentService.getCommentList(boardId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("commentList", commentList));
    }

    /**
     * 댓글 삭제
     *
     * @param commentId
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deleteComment(@PathVariable Long commentId,
                                                         @LoginUser String username,
                                                         @RequestBody(required = false) GuestPasswordCheckRequest guestPasswordCheckRequest
    ) throws NoSuchAlgorithmException {

        commentService.checkOwner(commentId, username, guestPasswordCheckRequest);
        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }
}
