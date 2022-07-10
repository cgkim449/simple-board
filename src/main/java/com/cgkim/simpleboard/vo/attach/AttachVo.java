package com.cgkim.simpleboard.vo.attach;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 첨부파일 VO
 */
@Data
@NoArgsConstructor
public class AttachVo {

    private Long attachId;

    private Long boardId;

    private Long questionId;

    private Long answerId;

    private Long faqId;

    private Long noticeId;

    private String uploadPath;

    private String uuid;

    private String name;
    private String extension; // 확장자

    private boolean isImage; // 이미지 여부

    private long size;

    /**
     * 이미지 여부
     *
     * @return boolean
     */
    public boolean getIsImage() {
        return isImage;
    }

    /**
     * 이미지 여부
     *
     * @return boolean
     */
    public boolean isImage() {
        return isImage;
    }

    private String thumbnailUri;
    private String originalImageUri;

    @Builder
    public AttachVo(Long attachId, Long boardId, Long questionId, Long answerId, Long faqId, Long noticeId, String uploadPath, String uuid, String name, String extension, boolean isImage, long size, String thumbnailUri, String originalImageUri) {

        this.attachId = attachId;
        this.boardId = boardId;
        this.questionId = questionId;
        this.answerId = answerId;
        this.faqId = faqId;
        this.noticeId = noticeId;
        this.uploadPath = uploadPath;
        this.uuid = uuid;
        this.name = name;
        this.extension = extension;
        this.isImage = isImage;
        this.size = size;
        this.thumbnailUri = thumbnailUri;
        this.originalImageUri = originalImageUri;
    }

    /**
     * 첨부파일 이름 + 확장자 리턴
     *
     * @return String
     */
    public String getFullName() {
        return getName() + '.' + getExtension();
    }


}
