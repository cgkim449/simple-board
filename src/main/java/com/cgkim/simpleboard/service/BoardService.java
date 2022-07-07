package com.cgkim.simpleboard.service;

import com.cgkim.simpleboard.domain.board.Board;
import com.cgkim.simpleboard.domain.board.BoardRepository;
import com.cgkim.simpleboard.exception.BoardInsertFailedException;
import com.cgkim.simpleboard.exception.BoardNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.vo.attach.AttachVo;
import com.cgkim.simpleboard.vo.board.BoardDetailResponse;
import com.cgkim.simpleboard.vo.board.BoardListResponse;
import com.cgkim.simpleboard.vo.board.BoardSaveRequest;
import com.cgkim.simpleboard.vo.board.BoardUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 익명 게시물 작성
     *
     * @param boardSaveRequest
     * @param attachInsertList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long writeBoard(BoardSaveRequest boardSaveRequest, List<AttachVo> attachInsertList) {

        try {
            Long savedBoardId = boardRepository.save(boardSaveRequest.toBoard()).getBoardId();

            if (isNotEmpty(attachInsertList)) {
//TODO: 첨부파일
//                insertAttaches(attachInsertList, savedBoardId);  //첨부파일 insert
//                updateHasAttach(savedBoardId); //첨부파일 유무 update
//                updateThumbnailUri(attachInsertList, savedBoardId); //썸네일 URI update
            }

            return savedBoardId;

        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제

            e.printStackTrace();
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }

    private boolean isNotEmpty(List<AttachVo> attachList) {
        return attachList != null && !attachList.isEmpty();
    }


    /**
     * 게시물 상세 조회
     *
     * @param boardId
     * @return BoardDetailResponse
     */
    public BoardDetailResponse viewBoardDetail(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        return BoardDetailResponse.from(board);
    }

    /**
     * 게시물 수정
     *
     * @param boardId
     * @param boardUpdateRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long updateBoard(Long boardId,
                            BoardUpdateRequest boardUpdateRequest) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        board.update(boardUpdateRequest.getTitle(), boardUpdateRequest.getContent());

        return boardId;
    }

    /**
     * 게시물 삭제
     *
     * @param boardId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        //TODO: 수정
        boardRepository.deleteById(boardId);
    }

    /**
     * 게시물 목록 조회
     *
     * @return
     */
    //TODO: 검색, 페이징
    public List<BoardListResponse> viewBoardList() {

        List<Board> allBoards = boardRepository.findAll();

        List<BoardListResponse> boardListResponseList = new ArrayList<>();

        for (Board board : allBoards) {
            boardListResponseList.add(BoardListResponse.from(board));
        }

        return boardListResponseList;
    }

    //TODO: 검색
    public long getTotalCount() {

        long count = boardRepository.count();

        return count;
    }
}