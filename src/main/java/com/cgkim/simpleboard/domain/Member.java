package com.cgkim.simpleboard.domain;

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
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

//TODO: one to many, 연관관계 편의 메서드 (다른 도메인들도 전부)

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @Column(unique = true, length = 30, nullable = false)
    private String username;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(unique = true, length = 30, nullable = false)
    private String nickname;

    @CreatedDate
    private LocalDateTime registerDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @Builder
    public Member(Long memberId,
                  List<Board> boards,
                  List<Comment> comments,
                  String username,
                  String password,
                  String nickname,
                  LocalDateTime registerDate,
                  LocalDateTime updateDate
    ) {

        this.memberId = memberId;
        this.boards = boards;
        this.comments = comments;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }
}