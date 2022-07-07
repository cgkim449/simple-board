package com.cgkim.simpleboard.domain.board;

import com.cgkim.simpleboard.domain.boardAttach.BoardAttach;
import com.cgkim.simpleboard.domain.category.Category;
import com.cgkim.simpleboard.domain.comment.Comment;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "board")
    private List<BoardAttach> attaches;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String guestNickname;

    @CreatedDate
    private LocalDateTime registerDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @Builder
    public Board(Category category, String title, String content, String guestNickname) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.guestNickname = guestNickname;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}