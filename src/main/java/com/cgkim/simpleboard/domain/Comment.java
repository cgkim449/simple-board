package com.cgkim.simpleboard.domain;

import com.cgkim.simpleboard.util.SHA256PasswordEncoder;
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
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

/**
 * 테이블 매핑
 *  - Comment 테이블
 *
 * 연관관계 매핑
 *  - 다대일 : Board, Member, Admin
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String guestNickname;
    private String guestPassword;

    @CreatedDate
    private Date registerDate;

    @LastModifiedDate
    private Date updateDate;

    @Builder
    private Comment(Long commentId,
                    Board board,
                    Member member,
                    Admin admin,
                    String content,
                    String guestNickname,
                    String guestPassword,
                    Date registerDate,
                    Date updateDate
    ) {

        this.commentId = commentId;
        this.board = board;
        this.member = member;
        this.admin = admin;
        this.content = content;
        this.guestNickname = guestNickname;
        this.guestPassword = guestPassword;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }

    /**
     * Comment 생성 (익명 댓글)
     *
     * @param board
     * @param content
     * @param guestNickname
     * @param guestPassword
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Comment createComment(Board board,
                                        String content,
                                        String guestNickname,
                                        String guestPassword
    ) throws NoSuchAlgorithmException {

        Comment comment = Comment.builder()
                .content(content)
                .guestNickname(guestNickname)
                .guestPassword(SHA256PasswordEncoder.getHash(guestPassword))
                .build();

        comment.setBoard(board);

        return comment;
    }

    /**
     * Comment 생성 (회원 댓글)
     *
     * @param board
     * @param member
     * @param content
     * @return
     */
    public static Comment createComment(Board board,
                                        Member member,
                                        String content
    ) {

        Comment comment = Comment.builder()
                .content(content)
                .build();

        comment.setBoard(board);
        comment.setMember(member);

        return comment;
    }

    /**
     * 양방향 참조값 넣어줌
     *
     * @param board
     */
    public void setBoard(Board board) {

        if (this.board != null) {
            this.board.getComments().remove(this);
        }

        this.board = board;

        if (!board.getComments().contains(this)) {
            board.getComments().add(this);
        }
    }

    /**
     * 위와 같음
     *
     * @param member
     */
    public void setMember(Member member) {

        if (this.member != null) {
            this.member.getComments().remove(this);
        }

        this.member = member;

        if (!member.getComments().contains(this)) {
            member.getComments().add(this);
        }
    }

    /**
     * 익명 댓글 비밀번호 검증
     *
     * @param guestPassword
     * @return
     * @throws NoSuchAlgorithmException
     */
    public boolean isPasswordMismatch(String guestPassword) throws NoSuchAlgorithmException {
        return !this.guestPassword.equals(SHA256PasswordEncoder.getHash(guestPassword));
    }
}