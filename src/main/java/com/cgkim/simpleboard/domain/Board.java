package com.cgkim.simpleboard.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

//    가급적 양방향보단 단방향이 나음.
//    '게시물을 등록할때 항상 첨부파일이 등록된다'가 아니라, '첨부파일이 등록될때 게시물이 필요하다' 라고 생각하는게 더 맞다
//
//    @OneToMany(mappedBy = "board")
//    private List<Attach> attaches = new ArrayList<>();
//
//    @OneToMany(mappedBy = "board")
//    private List<Comment> comments = new ArrayList<>();

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String guestNickname;

    private String guestPassword;

    private Integer viewCount;

    private Integer hasAttach;

    private String thumbnailUri;

    @CreatedDate
    private Date registerDate;

    @LastModifiedDate
    private Date updateDate;

    @Builder
    public Board(Long boardId,
                 Category category,
                 Member member,
                 Admin admin,
                 String title,
                 String content,
                 String guestNickname,
                 String guestPassword,
                 Integer viewCount,
                 Integer hasAttach,
                 String thumbnailUri,
                 Date registerDate,
                 Date updateDate
    ) {

        this.boardId = boardId;
        this.category = category;
        this.member = member;
        this.admin = admin;
        this.title = title;
        this.content = content;
        this.guestNickname = guestNickname;
        this.guestPassword = guestPassword;
        this.viewCount = viewCount;
        this.hasAttach = hasAttach;
        this.thumbnailUri = thumbnailUri;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateHasAttach(Integer hasAttach) {
        this.hasAttach = hasAttach;
    }

    public void updateThumbnailUri(String thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }
}