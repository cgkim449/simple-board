package com.cgkim.simpleboard.controller;

import com.cgkim.simpleboard.argumentresolver.LoginUser;
import com.cgkim.simpleboard.dto.common.GuestPasswordCheckRequest;
import com.cgkim.simpleboard.exception.LoginRequiredException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.response.SuccessResponse;
import com.cgkim.simpleboard.service.AttachService;
import com.cgkim.simpleboard.service.BoardService;
import com.cgkim.simpleboard.service.MemberService;
import com.cgkim.simpleboard.util.FileHandler;
import com.cgkim.simpleboard.dto.attach.AttachDto;
import com.cgkim.simpleboard.dto.board.BoardDetailResponse;
import com.cgkim.simpleboard.dto.board.BoardListResponse;
import com.cgkim.simpleboard.dto.board.BoardSaveRequest;
import com.cgkim.simpleboard.dto.board.BoardSearchRequest;
import com.cgkim.simpleboard.dto.board.BoardUpdateRequest;
import com.cgkim.simpleboard.dto.common.FileSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.List;

//TODO: repository 인터페이스로 바꾸기

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    private final AttachService attachService;

    private final MemberService memberService;

    private final FileHandler fileHandler;


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
     * 익명 게시물 작성
     *
     * @param boardSaveRequest
     * @return
     */
    @PostMapping("/guest")
    public ResponseEntity<SuccessResponse> writeBoard(@Valid BoardSaveRequest boardSaveRequest,
                                                      @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        List<AttachDto> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //물리적 파일 생성
        Long boardId = boardService.writeGuestBoard(boardSaveRequest, attachInsertList);

        return ResponseEntity.created(URI.create("/boards/" + boardId)).body(new SuccessResponse());
    }

    /**
     * 회원 게시물 작성
     *
     * @param username
     * @param boardSaveRequest
     * @param fileSaveRequest
     * @return ResponseEntity<SuccessResponse>
     * @throws IOException
     */
    @PostMapping("/member")
    public ResponseEntity<SuccessResponse> writeBoard(@LoginUser String username,
                                                      @Valid BoardSaveRequest boardSaveRequest,
                                                      @Valid FileSaveRequest fileSaveRequest
    ) throws IOException {

        if (username == null) {
            throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
        }

        memberService.getMemberByUsername(username);

        List<AttachDto> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles()); //물리적 파일 생성

        Long boardId = boardService.writeMemberBoard(username, boardSaveRequest, attachInsertList); //글 작성

        return ResponseEntity.created(URI.create("/boards/" + boardId)).body(new SuccessResponse());
    }

    /**
     * 게시물 삭제
     *
     * @param boardId
     * @return
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<SuccessResponse> deleteBoard(@PathVariable Long boardId,
                                                       @LoginUser String username,
                                                       @RequestBody(required = false) GuestPasswordCheckRequest guestPasswordCheckRequest
    ) throws NoSuchAlgorithmException  {

        boardService.checkOwner(boardId, username, guestPasswordCheckRequest);

        List<AttachDto> deleteAttaches = boardService.deleteBoard(boardId);
        fileHandler.deleteFiles(deleteAttaches);

        return ResponseEntity.noContent().build();
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
                                                       @LoginUser String username,
                                                       @Valid BoardUpdateRequest boardUpdateRequest,
                                                       @Valid FileSaveRequest fileSaveRequest,
                                                       GuestPasswordCheckRequest guestPasswordCheckRequest,
                                                       Long[] attachDeleteRequest
    ) throws IOException, NoSuchAlgorithmException {

        boardService.checkOwner(boardId, username, guestPasswordCheckRequest);

        List<AttachDto> attachInsertList = fileHandler.createFiles(fileSaveRequest.getMultipartFiles());
        List<AttachDto> attachDeleteList = boardService.updateBoard(boardId, boardUpdateRequest, attachInsertList, attachDeleteRequest);

        fileHandler.deleteFiles(attachDeleteList); //물리적 파일 삭제

        return ResponseEntity.ok(new SuccessResponse());
    }

    /**
     * 익명 글 비밀번호 확인
     *
     * @param guestPasswordCheckRequest
     * @param boardId
     * @return ResponseEntity<SuccessResponse>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/{boardId}/password-check")
    public ResponseEntity<SuccessResponse> checkGuestPassword(@PathVariable Long boardId,
                                                              @RequestBody GuestPasswordCheckRequest guestPasswordCheckRequest
    ) throws NoSuchAlgorithmException {

        boardService.checkGuestPassword(boardId, guestPasswordCheckRequest);

        return ResponseEntity.ok(new SuccessResponse());
    }
}
