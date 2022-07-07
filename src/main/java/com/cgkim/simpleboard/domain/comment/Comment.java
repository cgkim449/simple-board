package com.cgkim.simpleboard.domain.comment;

import com.cgkim.simpleboard.domain.board.Board;
import com.cgkim.simpleboard.domain.boardAttach.BoardAttach;
import com.cgkim.simpleboard.domain.category.Category;
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
import java.util.List;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String guestNickname;

    @CreatedDate
    private LocalDateTime registerDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @Builder
    public Comment(Long commentId, Board board, String content, String guestNickname) {
        this.commentId = commentId;
        this.board = board;
        this.content = content;
        this.guestNickname = guestNickname;
    }
}