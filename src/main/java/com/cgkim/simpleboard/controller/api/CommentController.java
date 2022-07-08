package com.cgkim.simpleboard.controller.api;

import com.cgkim.simpleboard.response.SuccessResponse;
import com.cgkim.simpleboard.service.CommentService;
import com.cgkim.simpleboard.vo.comment.CommentSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

/**
 * 자유게시판 댓글 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@RestController
public class CommentController {

    private final CommentService commentService;


    /**
     * 익명 댓글 작성
     *
     * @param commentSaveRequest
     * @return
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/guest")
    public ResponseEntity<SuccessResponse> writeComment(@Valid CommentSaveRequest commentSaveRequest) {

        Long commentId = commentService.writeComment(commentSaveRequest);

        return ResponseEntity.created(URI.create("/comments/" + commentId)).body(new SuccessResponse());
    }

    /**
     * 댓글 삭제
     *
     * @param commentId
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deleteComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }

//    /**
//     * 댓글 목록 조회
//     *
//     * @param boardId
//     * @return ResponseEntity<SuccessResponse>
//     */
//    @GetMapping
//    public ResponseEntity<SuccessResponse> getCommentList(Long boardId) {
//
//        List<CommentListResponse> commentList = commentService.getCommmentList(boardId);
//
//        return ResponseEntity
//                .ok(new SuccessResponse()
//                        .put("commentList", commentList));
//    }
}
