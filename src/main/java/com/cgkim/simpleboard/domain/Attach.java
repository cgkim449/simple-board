package com.cgkim.simpleboard.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

/**
 * 테이블 매핑
 *  - Attach 테이블
 *
 * 연관관계 매핑
 *  - 다대일 : Board
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Attach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(length = 200, nullable = false)
    private String uploadPath;

    @Column(length = 100, nullable = false)
    private String uuid;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String extension;

    private Boolean isImage;

    @Column(nullable = false)
    private Long size;

    @CreatedDate
    private LocalDateTime registerDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @Builder
    public Attach(Long attachId,
                  Board board,
                  String uploadPath,
                  String uuid,
                  String name,
                  String extension,
                  Boolean isImage,
                  Long size,
                  LocalDateTime registerDate,
                  LocalDateTime updateDate
    ) {

        this.attachId = attachId;
        this.board = board;
        this.uploadPath = uploadPath;
        this.uuid = uuid;
        this.name = name;
        this.extension = extension;
        this.isImage = isImage;
        this.size = size;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }

    /**
     * Attach 생성
     *
     * @param board
     * @return
     */
    public static Attach createAttach(Board board) {
        Attach attach = new Attach();
        attach.setBoard(board);

        //TODO: attach 에 넣을 것들 set 하거나 update 메서드 만들어서 하자

        return attach;
    }

    /**
     * 양방향 참조값 넣어줌
     *
     * @param board
     */
    public void setBoard(Board board) {

        if (this.board != null) {
            this.board.getAttaches().remove(this);
        }

        this.board = board;

        if(!board.getAttaches().contains(this)) {
            board.getAttaches().add(this);
        }
    }

    public String getFullName() {
        return getName() + '.' + getExtension();
    }

}