package com.cgkim.simpleboard.dto.board;

import com.cgkim.simpleboard.domain.Attach;
import com.cgkim.simpleboard.domain.Board;
import com.cgkim.simpleboard.domain.Comment;
import com.cgkim.simpleboard.dto.attach.AttachDto;
import com.cgkim.simpleboard.dto.comment.CommentListResponse;
import com.cgkim.simpleboard.util.AttachURIProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 게시물 상세 조회 응답
 */
@Getter @Setter
public class BoardDetailResponse {

    private Long boardId;

    private String title;

    private String content;

    private int viewCount;

    private Boolean hasAttach;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date registerDate;

    @JsonFormat(pattern="yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDate;

    private Long categoryId;

    private String categoryName;

    private String guestNickname;

    private String memberUsername;

    private String memberNickname;

    private String adminUsername;

    private String adminNickname;

    private List<CommentListResponse> commentList;

    private List<AttachDto> attachList;

    @Builder
    public BoardDetailResponse(Long boardId, String title, String content, int viewCount, Boolean hasAttach, Date registerDate, Date updateDate, Long categoryId, String categoryName, String guestNickname, String memberUsername, String memberNickname, String adminUsername, String adminNickname, List<CommentListResponse> commentList, List<AttachDto> attachList) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.hasAttach = hasAttach;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.guestNickname = guestNickname;
        this.memberUsername = memberUsername;
        this.memberNickname = memberNickname;
        this.adminUsername = adminUsername;
        this.adminNickname = adminNickname;
        this.commentList = commentList;
        this.attachList = attachList;
    }

    public static BoardDetailResponse from(Board board) {

        BoardDetailResponse boardDetailResponse = BoardDetailResponse.builder()
                .boardId(board.getBoardId())
                .categoryId(board.getCategory().getCategoryId())
                .categoryName(board.getCategory().getName())
                .hasAttach(board.getHasAttach())
                .title(board.getTitle())
                .content(board.getContent())
                .guestNickname(board.getGuestNickname())
                .memberNickname(board.getMember() != null ? board.getMember().getNickname() : null)
                .memberUsername(board.getMember() != null ? board.getMember().getUsername() : null)
                .adminNickname(board.getAdmin() != null ? board.getAdmin().getNickname() : null)
                .adminUsername(board.getAdmin() != null ? board.getAdmin().getUsername() : null)
                .viewCount(board.getViewCount())
                .registerDate(board.getRegisterDate())
                .updateDate(board.getUpdateDate())
                .build();

        //TODO: 다시?
        List<AttachDto> attachDtoList = toAttachDTOs(board.getAttaches(), board.getBoardId());
        AttachURIProvider.setImageURIsOf(attachDtoList);
        boardDetailResponse.setAttachList(attachDtoList);

        List<CommentListResponse> commentListResponses = toCommentDTOs(board);
        boardDetailResponse.setCommentList(commentListResponses);
        
        return boardDetailResponse;
    }

    private static List<CommentListResponse> toCommentDTOs(Board board) {

        List<CommentListResponse> commentListResponses = new ArrayList<>();

        for (Comment comment : board.getComments()) {
            commentListResponses.add(CommentListResponse.from(comment));
        }

        return commentListResponses;
    }

    private static List<AttachDto> toAttachDTOs(List<Attach> attaches, Long boardId) {

        List<AttachDto> attachDtoList = new ArrayList<>();

        for (Attach attach : attaches) {
            attachDtoList.add(AttachDto.builder()
                    .attachId(attach.getAttachId())
                    .boardId(boardId)
                    .uploadPath(attach.getUploadPath())
                    .uuid(attach.getUuid())
                    .name(attach.getName())
                    .extension(attach.getExtension())
                    .isImage(attach.getIsImage())
                    .size(attach.getSize())
                    .build());
        }

        return attachDtoList;
    }
}
