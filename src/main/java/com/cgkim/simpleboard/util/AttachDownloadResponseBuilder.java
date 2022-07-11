package com.cgkim.simpleboard.util;

import com.cgkim.simpleboard.exception.AttachNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import com.cgkim.simpleboard.dto.attach.AttachDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 첨부파일 다운로드 요청의 응답메시지를 빌드하는 역할
 */
@RequiredArgsConstructor
@Component
public class AttachDownloadResponseBuilder {


    /**
     * 첨부파일 다운로드 요청의 응답메시지를 빌드하는 역할
     *
     * @param attachDto
     * @return ResponseEntity<Resource>
     */
    public ResponseEntity<Resource> buildResponseWith(AttachDto attachDto) {

        Resource resource = findResourceOf(attachDto);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition
                                .attachment()
                                .filename(attachDto.getFullName(), StandardCharsets.UTF_8)
                                .build()
                                .toString())
                .body(resource);
    }

    private Resource findResourceOf(AttachDto attachDto) {

        Resource resource = new FileSystemResource(AttachURIProvider.getAbsolutePathOf(attachDto));

        if (!resource.exists()) {
            throw new AttachNotFoundException(ErrorCode.ATTACH_NOT_FOUND);
        }

        return resource;
    }


}
