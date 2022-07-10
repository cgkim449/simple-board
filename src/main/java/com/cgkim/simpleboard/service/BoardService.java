package com.cgkim.simpleboard.service;

import com.cgkim.simpleboard.domain.Attach;
import com.cgkim.simpleboard.domain.Board;
import com.cgkim.simpleboard.exception.BoardInsertFailedException;
import com.cgkim.simpleboard.exception.BoardNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.repository.AttachRepository;
import com.cgkim.simpleboard.repository.BoardRepository;
import com.cgkim.simpleboard.util.AttachURIProvider;
import com.cgkim.simpleboard.util.SHA256PasswordEncoder;
import com.cgkim.simpleboard.vo.attach.AttachVo;
import com.cgkim.simpleboard.vo.board.BoardDetailResponse;
import com.cgkim.simpleboard.vo.board.BoardListResponse;
import com.cgkim.simpleboard.vo.board.BoardSaveRequest;
import com.cgkim.simpleboard.vo.board.BoardSearchRequest;
import com.cgkim.simpleboard.vo.board.BoardUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final AttachRepository attachRepository;

    private final SHA256PasswordEncoder sha256PasswordEncoder;

    private final AttachURIProvider attachURIProvider;


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
            Board board = boardSaveRequest.toBoard();
            boardRepository.save(board); //글 저장
            Long savedBoardId = board.getBoardId();

            if (isNotEmpty(attachInsertList)) {

                insertAttaches(attachInsertList, board);  //첨부파일 insert

                updateHasAttach(board);
                updateThumbnailUri(attachInsertList, board); //썸네일 URI update
            }

            return savedBoardId;

        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제

            e.printStackTrace();
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }

    private void updateThumbnailUri(List<AttachVo> attachList, Board board) {

        for (AttachVo attach : attachList) {

            if (attach.isImage()) {

                String thumbnailUri = attachURIProvider.createThumbnailURIForDB(attach);

                board.updateThumbnailUri(thumbnailUri);

                return;
            }
        }

        board.updateThumbnailUri("");
    }

    private void updateHasAttach(Board board) {
        //TODO: select count 로 바꾸기
        List<Attach> attaches = attachRepository.findAll(board.getBoardId());
        Integer hasAttach = attaches.size() > 0 ? 1 : 0;

        board.updateHasAttach(hasAttach);
    }

    private void insertAttaches(List<AttachVo> attachInsertList, Board board) {

        for (AttachVo attachVo : attachInsertList) {

            Attach attach = Attach.builder()
                    .attachId(attachVo.getAttachId())
                    .board(board)
                    .uploadPath(attachVo.getUploadPath())
                    .uuid(attachVo.getUuid())
                    .name(attachVo.getName())
                    .extension(attachVo.getExtension())
                    .isImage(attachVo.getIsImage() ? 1 : 0)
                    .size(attachVo.getSize())
                    .build();

            attachRepository.save(attach);
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
    //TODO: 조회수 증가랑 분리해야하나(트랜잭션때문에)
    @Transactional(rollbackFor = Exception.class)
    public BoardDetailResponse viewBoardDetail(Long boardId) {

        Board board = boardRepository.findById(boardId);

        if(board == null) {
            throw new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

        List<Attach> attaches = board.getAttaches();

        List<AttachVo> attachVoList = new ArrayList<>();

        //TODO: 다시해야됨
        for (Attach attach : attaches) {
            attachVoList.add(AttachVo.builder()
                    .attachId(attach.getAttachId())
                    .boardId(board.getBoardId())
                    .uploadPath(attach.getUploadPath())
                    .uuid(attach.getUuid())
                    .name(attach.getName())
                    .extension(attach.getExtension())
                    .isImage(attach.getIsImage() == 1)
                    .size(attach.getSize())
                    .build());
        }

        attachURIProvider.setImageURIsOf(attachVoList);
        BoardDetailResponse boardDetailResponse = BoardDetailResponse.from(board);
        boardDetailResponse.setAttachList(attachVoList);

        return boardDetailResponse;
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
                            BoardUpdateRequest boardUpdateRequest
    ) {

        Board board = boardRepository.findById(boardId);

        if(board == null) {
            throw new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

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
        boardRepository.deleteById(boardId);
    }

    /**
     * 게시물 목록 조회
     *
     * @return
     */
    public List<BoardListResponse> viewBoardList(BoardSearchRequest boardSearchRequest) {

        List<Board> allBoards = boardRepository.findAll(boardSearchRequest);

        List<BoardListResponse> boardListResponses = new ArrayList<>();

        for (Board board : allBoards) {
            boardListResponses.add(BoardListResponse.from(board));
        }

        return boardListResponses;
    }

    //TODO: 검색
    public long getTotalCount(BoardSearchRequest boardSearchRequest) {

        long count = boardRepository.count(boardSearchRequest);

        return count;
    }
}