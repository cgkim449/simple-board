package com.cgkim.simpleboard.service;

import com.cgkim.simpleboard.domain.Attach;
import com.cgkim.simpleboard.domain.Board;
import com.cgkim.simpleboard.domain.Category;
import com.cgkim.simpleboard.domain.Member;
import com.cgkim.simpleboard.dto.common.GuestPasswordCheckRequest;
import com.cgkim.simpleboard.exception.BoardInsertFailedException;
import com.cgkim.simpleboard.exception.GuestPasswordInvalidException;
import com.cgkim.simpleboard.exception.GuestPasswordMismatchException;
import com.cgkim.simpleboard.exception.LoginRequiredException;
import com.cgkim.simpleboard.exception.NoAuthorizationException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.repository.BoardRepository;
import com.cgkim.simpleboard.repository.CategoryRepository;
import com.cgkim.simpleboard.dto.attach.AttachDto;
import com.cgkim.simpleboard.dto.board.BoardDetailResponse;
import com.cgkim.simpleboard.dto.board.BoardListResponse;
import com.cgkim.simpleboard.dto.board.BoardSaveRequest;
import com.cgkim.simpleboard.dto.board.BoardSearchRequest;
import com.cgkim.simpleboard.dto.board.BoardUpdateRequest;
import com.cgkim.simpleboard.repository.MemberRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * BoardService
 * - 비즈니스 로직은 전부 도메인에 있음
 * - Service 는 트랜잭션만 관리함
 */
@RequiredArgsConstructor
@Transactional(readOnly = true) //동기화 생략. 데이터를 조회만 하는 트랜잭션에서는 동기화할 필요가 없으므로.
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final MemberRepository memberRepository;

    private final CategoryRepository categoryRepository;


    /**
     * 회원 게시물 작성
     *
     * @param username
     * @param boardSaveRequest
     * @param attachInsertList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long writeMemberBoard(String username, BoardSaveRequest boardSaveRequest, List<AttachDto> attachInsertList) {

        try {
            //Member 엔티티 조회
            Member member = memberRepository.findByUsername(username);

            //Category 엔티티 조회
            Category category = categoryRepository.findByCategoryId(boardSaveRequest.getCategoryId());
            boardCategoryRepository.findByBoardId()

            //AttachDto -> Attach 엔티티
            List<Attach> insertAttaches = toAttaches(attachInsertList);

            //Board 엔티티 생성
            Board board = Board.createBoard(
                    member,
                    boardCategories,
                    insertAttaches,
                    boardSaveRequest.getTitle(),
                    boardSaveRequest.getContent()
            );

            board.updateHasAttach(); //첨부파일 유무 업데이트
            board.updateThumbnailUri(); //썸네일 URI 업데이트

            boardRepository.save(board); //Board 저장

            return board.getBoardId();

        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제

            e.printStackTrace();
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }

    /**
     * 익명 게시물 작성
     *
     * @param boardSaveRequest
     * @param attachInsertList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long writeGuestBoard(BoardSaveRequest boardSaveRequest, List<AttachDto> attachInsertList) {

        try {
            //Category 엔티티 조회
            Category category = categoryRepository.findByCategoryId(boardSaveRequest.getCategoryId());

            //AttachDto -> Attach 엔티티
            List<Attach> insertAttaches = toAttaches(attachInsertList);

            //Board 엔티티 생성
            Board board = Board.createBoard(
                    category,
                    insertAttaches,
                    boardSaveRequest.getTitle(),
                    boardSaveRequest.getContent(),
                    boardSaveRequest.getGuestNickname(),
                    boardSaveRequest.getGuestPassword()
            );

            board.updateHasAttach(); //첨부파일 유무 업데이트
            board.updateThumbnailUri(); //썸네일 URI 업데이트

            boardRepository.save(board); //Board 저장

            return board.getBoardId();

        } catch (Exception e) { //게시물 등록 실패시 생성했던 파일 삭제

            e.printStackTrace();
            throw new BoardInsertFailedException(attachInsertList, ErrorCode.BOARD_INSERT_FAILED);
        }
    }

    private List<Attach> toAttaches(List<AttachDto> attachInsertList) {

        List<Attach> insertAttaches =  new ArrayList<>();

        if(attachInsertList != null) {
            for (AttachDto attachDto : attachInsertList) {
                insertAttaches.add(attachDto.toAttach());
            }
        }

        return insertAttaches;
    }

    /**
     * 게시물 상세 조회
     *
     * @param boardId
     * @return BoardDetailResponse
     */
    @Transactional(rollbackFor = Exception.class)
    public BoardDetailResponse viewBoardDetail(Long boardId) {

        Board board = boardRepository.findById(boardId);

        board.increaseViewCount(); //조회수 1 증가

        return BoardDetailResponse.from(board);
    }

    /**
     * 게시물 삭제
     *
     * @param boardId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<AttachDto> deleteBoard(Long boardId) {
        return boardRepository.deleteByBoardId(boardId);
    }

    /**
     * 게시물 목록 조회
     *
     * @param boardSearchRequest
     * @return
     */
    public List<BoardListResponse> viewBoardList(BoardSearchRequest boardSearchRequest) {

        return boardRepository.findAll(boardSearchRequest);
    }




    /**
     * 게시물 갯수 조회
     *
     * @param boardSearchRequest
     * @return
     */
    public Long getTotalCount(BoardSearchRequest boardSearchRequest) {

        return boardRepository.count(boardSearchRequest);
    }

    /**
     * 게시물 수정
     *
     * @param boardId
     * @param boardUpdateRequest
     * @param attachInsertList
     * @param attachDeleteRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<AttachDto> updateBoard(Long boardId,
                                       BoardUpdateRequest boardUpdateRequest,
                                       List<AttachDto> attachInsertList,
                                       Long[] attachDeleteRequest
    ) {

        //AttachDto -> Attach 엔티티
        List<Attach> insertAttaches = toAttaches(attachInsertList);

        Board board = boardRepository.findById(boardId);

        board.update(boardUpdateRequest.getTitle(), boardUpdateRequest.getContent(), insertAttaches, attachDeleteRequest);

        board.updateHasAttach(); //첨부파일 유무 업데이트
        board.updateThumbnailUri(); //썸네일 URI 업데이트

        //TODO: attachDto return
        return null;
    }

    /**
     * 게시물 소유권 인증
     *
     * @param boardId
     * @param username
     * @param guestPasswordCheckRequest
     * @throws NoSuchAlgorithmException
     */
    public void checkOwner(Long boardId, String username, GuestPasswordCheckRequest guestPasswordCheckRequest) throws NoSuchAlgorithmException {

        Board board = boardRepository.findById(boardId);

        if (board.getAdmin() != null) {
            throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);

        } else if (board.getGuestNickname() != null) {

            validateGuestPassword(guestPasswordCheckRequest);
            checkGuestPassword(boardId, guestPasswordCheckRequest);

        } else if (board.getMember() != null) {

            if (username == null) {
                throw new LoginRequiredException(ErrorCode.LOGIN_REQUIRED);
            }

            Member member = memberRepository.findByUsername(username);

            if (member == null) {
                throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION);
            }

            //getMemberId() 안해도됨
            if (Objects.equals(board.getMember().getMemberId(), member.getMemberId())) {
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
     * @param boardId
     * @param guestPasswordCheckRequest
     * @throws NoSuchAlgorithmException
     */
    public void checkGuestPassword(Long boardId, GuestPasswordCheckRequest guestPasswordCheckRequest) throws NoSuchAlgorithmException {

        String guestPassword = guestPasswordCheckRequest.getGuestPassword();

        Board board = boardRepository.findById(boardId);

        if (board.isPasswordMismatch(guestPassword)) {
            throw new GuestPasswordMismatchException(ErrorCode.GUEST_PASSWORD_MISMATCH);
        }
    }
}