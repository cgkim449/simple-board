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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Category {

    @Id
    private Long categoryId;

    @OneToMany(mappedBy = "category")
    private List<Board> boards = new ArrayList<>();

    @Column(length = 100, nullable = false)
    private String name;

    @CreatedDate
    private LocalDateTime registerDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @Builder
    public Category(Long categoryId, List<Board> boards, String name, LocalDateTime registerDate, LocalDateTime updateDate) {
        this.categoryId = categoryId;
        this.boards = boards;
        this.name = name;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }
}
