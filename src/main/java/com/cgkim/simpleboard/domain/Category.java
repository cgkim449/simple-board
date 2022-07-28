package com.cgkim.simpleboard.domain;

import lombok.AccessLevel;
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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 테이블 매핑
 *  - Category 테이블
 *
 * 연관관계 매핑
 *  - 없음
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Category {

    @Id
    private Long categoryId;

    @Column(length = 100, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Board> boards = new ArrayList<>();

    //==카테고리 계층 구조==//
    //셀프로 양방향 연관관계를 걸어줌. 다른 엔티티랑 양방향 걸어줄때와 방법에 차이가 없음
    @ManyToOne
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


    @CreatedDate
    private LocalDateTime registerDate;

    @LastModifiedDate
    private LocalDateTime updateDate;
}
