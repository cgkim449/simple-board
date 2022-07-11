package com.cgkim.simpleboard.util;

import com.cgkim.simpleboard.dto.attach.AttachDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * 첨부파일 관련 URI 들을 제공하는 역할
 */
public class AttachURIProvider {

    //TODO: 프로퍼티 하고 중복됨
    private static final String staticResourceURL = "http://localhost:8082";

    private static final String thumbnailFileSuffix = "_thumbnail";

    private static final String uploadBasePath = "C://upload";


    /**
     * AttachVo 에 원본 이미지 URI 와 썸네일 URI 를 저장
     *
     * @param attachList
     */
    public static void setImageURIsOf(List<AttachDto> attachList) {

        for (AttachDto attachDto : attachList) {

            if (attachDto.isImage()) {
                setImageURIOf(attachDto);
            }
        }
    }

    private static void setImageURIOf(AttachDto attachDto) {

        attachDto.setThumbnailUri(createThumbnailURIOf(attachDto));
        attachDto.setOriginalImageUri(createOriginalImageURIOf(attachDto));
    }

    private static String createOriginalImageURIOf(AttachDto attachDto) {

        return staticResourceURL +
                File.separator +
                attachDto.getUploadPath() +
                File.separator +
                attachDto.getUuid() +
                "." +
                attachDto.getExtension();
    }

    private static String createThumbnailURIOf(AttachDto attachDto) {

        return staticResourceURL +
                File.separator +
                attachDto.getUploadPath() +
                File.separator +
                attachDto.getUuid() +
                thumbnailFileSuffix +
                "." +
                attachDto.getExtension();
    }

    /**
     * DB 에 저장할 thumbnail URI 생성
     *
     * @param uploadPath
     * @param uuid
     * @param extension
     * @return
     */
    public static String createThumbnailURIForDB(String uploadPath, String uuid, String extension) {

        return uploadPath + File.separator + uuid + thumbnailFileSuffix + "." + extension;
    }

    /**
     * 썸네일 정적 리소스 URI 생성
     *
     * @param thumbnailUri
     * @return String
     */
    public static String getFullURIOf(String thumbnailUri) {

        if(thumbnailUri == null) {
            return null;
        }
        return staticResourceURL + File.separator + thumbnailUri;
    }

    /**
     * 첨부파일의 절대경로 생성
     *
     * @param attachDto
     * @return String
     */
    public static String getAbsolutePathOf(AttachDto attachDto) {

        return uploadBasePath +
                File.separator +
                attachDto.getUploadPath() +
                File.separator +
                attachDto.getUuid() +
                '.' +
                attachDto.getExtension();
    }
}
