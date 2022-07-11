package com.cgkim.simpleboard.service;

import com.cgkim.simpleboard.domain.Attach;
import com.cgkim.simpleboard.dto.attach.AttachDto;
import com.cgkim.simpleboard.repository.AttachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 자유게시판 첨부파일 Service
 */
@RequiredArgsConstructor
@Service
public class AttachService {

    private final AttachRepository attachRepository;


    /**
     * 특정 첨부파일 조회
     *
     * @param attachId
     * @return AttachVo
     */
    public AttachDto getAttachDtoBy(Long attachId) {

        Attach attach = attachRepository.findByAttachId(attachId);

        return AttachDto.builder()
                .attachId(attach.getAttachId())
                .boardId(attach.getBoard().getBoardId())
                .uploadPath(attach.getUploadPath())
                .uuid(attach.getUuid())
                .name(attach.getName())
                .extension(attach.getExtension())
                .isImage(attach.getIsImage())
                .size(attach.getSize())
                .build();
    }

//    /**
//     * 특정 게시물의 첨부파일 리스트 조회
//     *
//     * @param boardId
//     * @return List<AttachVo>
//     */
//    public List<AttachVo> getList(Long boardId) {
//        return attachDao.selectList(boardId);
//    }
//
//
//    /**
//     * 첨부파일 id 배열로 첨부파일 리스트 조회
//     *
//     * @param attachIdArray
//     * @return List<AttachVo>
//     */
//    public List<AttachVo> getList(Long[] attachIdArray) {
//
//        if (attachIdArray == null || attachIdArray.length == 0) {
//            return null;
//        }
//
//        List<AttachVo> attachVoList = new ArrayList<>();
//
//        for (Long attachId : attachIdArray) {
//            attachVoList.add(attachDao.selectOne(attachId));
//        }
//
//        return attachVoList;
//    }
}