package com.cgkim.simpleboard.controller.api;

import com.cgkim.simpleboard.response.SuccessResponse;
import com.cgkim.simpleboard.service.BoardService;
import com.cgkim.simpleboard.util.FileHandler;
import com.cgkim.simpleboard.vo.attach.AttachVo;
import com.cgkim.simpleboard.vo.board.BoardDetailResponse;
import com.cgkim.simpleboard.vo.board.BoardListResponse;
import com.cgkim.simpleboard.vo.board.BoardSaveRequest;
import com.cgkim.simpleboard.vo.board.BoardSearchRequest;
import com.cgkim.simpleboard.vo.board.BoardUpdateRequest;
import com.cgkim.simpleboard.vo.common.FileSaveRequest;
import com.cgkim.simpleboard.vo.common.GuestPasswordCheckRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
public class BoardRestController {

    private final BoardService boardService;

    private final FileHandler fileHandler;

    /**
     * 익명 게시물 작성
     *
     * @param boardSaveRequest
     * @return
     */
    @PostMapping("/guest")
    public ResponseEntity<SuccessResponse> writeBoard(@Valid BoardSaveRequest boardSaveRequest,
                                                      @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        List<AttachVo> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //물리적 파일 생성
        Long boardId = boardService.writeBoard(boardSaveRequest, attachInsertList);

        return ResponseEntity.created(URI.create("/boards/" + boardId)).body(new SuccessResponse());
    }

    /**
     * 게시물 상세 조회
     *
     * @param boardId
     * @return
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> viewBoardDetail(@PathVariable Long boardId) {

        BoardDetailResponse boardDetail = boardService.viewBoardDetail(boardId);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("boardDetail", boardDetail));
    }

    /**
     * 게시물 수정
     *
     * @param boardId
     * @param boardUpdateRequest
     * @return
     */
    @PatchMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> updateBoard(@PathVariable Long boardId,
                                                       @Valid BoardUpdateRequest boardUpdateRequest
    ) {

        boardService.updateBoard(boardId, boardUpdateRequest);

        return ResponseEntity.ok(new SuccessResponse());
    }

    /**
     * 게시물 삭제
     *
     * @param boardId
     * @return
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> deleteBoard(@PathVariable Long boardId) {

        boardService.deleteBoard(boardId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 게시물 목록 조회
     *
     * @param boardSearchRequest
     * @return
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> viewBoardList(BoardSearchRequest boardSearchRequest) {

        List<BoardListResponse> boardList = boardService.viewBoardList(boardSearchRequest);
        long boardTotalCount = boardService.getTotalCount(boardSearchRequest);

        return ResponseEntity
                .ok(new SuccessResponse()
                        .put("boardList", boardList)
                        .put("boardTotalCount", boardTotalCount));
    }

}
