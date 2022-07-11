package com.cgkim.simpleboard.domain;

import com.cgkim.simpleboard.dto.attach.AttachDto;
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
import javax.persistence.Transient;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
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

    public String getFullName() {
        return getName() + '.' + getExtension();
    }

    //==연관관계 편의 메서드==//
    //연관관계 편의 메서드의 위치는 연관관계 주인인 쪽이 좋다?
    //양방향일때 연관관계 편의 메서드를 쓰면 편하다
    //자식에도 넣어주고 부모에도 넣어주는 역할을 함
    public void setBoard(Board board) {
        this.board = board;
        board.getAttaches().add(this);
        //이 두줄을 원자적으로 묶는것(그냥 편의에 의해 이렇게 하는것)
    }

    //==생성메서드==/
    public static Attach createAttach(Board board) {
        Attach attach = new Attach();
        attach.setBoard(board);

        //TODO: attach 에 넣을 것들 set 하거나 update 메서드 만들어서 하자

        return attach;
    }
}