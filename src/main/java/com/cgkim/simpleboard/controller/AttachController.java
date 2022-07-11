package com.cgkim.simpleboard.controller;

import com.cgkim.simpleboard.dto.attach.AttachDto;
import com.cgkim.simpleboard.service.AttachService;
import com.cgkim.simpleboard.util.AttachDownloadResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 자유게시판 첨부파일 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/board-attaches")
public class AttachController {

    private final AttachService attachService;

    private final AttachDownloadResponseBuilder attachDownloadResponseBuilder;

    /**
     * 첨부파일 다운로드
     *
     * @param attachId
     * @return ResponseEntity<Resource>
     */
    @GetMapping("/{attachId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long attachId) {

        AttachDto attachDto = attachService.getAttachDtoBy(attachId);

        return attachDownloadResponseBuilder.buildResponseWith(attachDto);
    }
}
