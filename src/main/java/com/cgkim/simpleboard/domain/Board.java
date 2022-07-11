package com.cgkim.simpleboard.domain;

import com.cgkim.simpleboard.util.AttachURIProvider;
import com.cgkim.simpleboard.util.SHA256PasswordEncoder;
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
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private final List<Attach> attaches = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String guestNickname;

    private String guestPassword;

    private Integer viewCount;

    private Boolean hasAttach;

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
                 Boolean hasAttach,
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



    /**
     * 제목, 내용 업데이트
     *
     * @param title
     * @param content
     * @param insertAttaches
     * @param attachDeleteRequest
     */
    public void update(String title, String content, List<Attach> insertAttaches, Long[] attachDeleteRequest) {
        this.title = title;
        this.content = content;

        Iterator<Attach> iterator = attaches.iterator();

        for (Long attachId : attachDeleteRequest) {
            while (iterator.hasNext()){
                Attach attach = iterator.next();

                if(Objects.equals(attach.getAttachId(), attachId)) {
                    iterator.remove();
                }
            }
        }

        if(insertAttaches != null) {
            for (Attach attach : insertAttaches) {
                addAttach(attach);
            }
        }
    }

    /**
     * 첨부파일 유무 업데이트
     */
    public void updateHasAttach() {
        if(attaches.size() > 0) {
            hasAttach = true;
        } else {
            hasAttach = false;
        }
    }

    /**
     * 썸네일 URI 업데이트
     */
    public void updateThumbnailUri() {
        if(hasAttach) {
            for (Attach attach : attaches) {
                if(attach.getIsImage()) {

                    this.thumbnailUri = AttachURIProvider.createThumbnailURIForDB(attach.getUploadPath(), attach.getUuid(), attach.getExtension());
                    return;
                }
            }
        }

        this.thumbnailUri = "";
    }

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        category.getBoards().add(this);
    }

    public void addAttach(Attach attach) {
        attaches.add(attach);
        attach.setBoard(this);
    }

    public static Board createBoard(Category category,
                                    List<Attach> insertAttaches,
                                    String title,
                                    String content,
                                    String guestNickname,
                                    String guestPassword
    ) throws NoSuchAlgorithmException {

        Board board = Board.builder()
                .title(title)
                .content(content)
                .guestNickname(guestNickname)
                .guestPassword(SHA256PasswordEncoder.getHash(guestPassword))
                .viewCount(0)
                .build();

        board.setCategory(category);

        if(insertAttaches != null) {
            for (Attach attach : insertAttaches) {
                board.addAttach(attach);
            }
        }

        return board;
    }

    public static Board createBoard(Member member,
                                    Category category,
                                    List<Attach> insertAttaches,
                                    String title,
                                    String content
    ) {

        Board board = Board.builder()
                .title(title)
                .content(content)
                .viewCount(0)
                .build();

        board.setMember(member);
        board.setCategory(category);

        if(insertAttaches != null) {
            for (Attach attach : insertAttaches) {
                board.addAttach(attach);
            }
        }

        return board;
    }

    public static Board createBoard(Category category, Member member, Attach... attaches) {
        Board board = new Board();
        board.setCategory(category);
        board.setMember(member);
        for (Attach attach : attaches) {
            board.addAttach(attach);
        }

        return board;
    }

    public void increaseViewCount() {
        viewCount++;
    }

    public boolean isPasswordMismatch(String guestPassword) throws NoSuchAlgorithmException {
        return !this.guestPassword.equals(SHA256PasswordEncoder.getHash(guestPassword));
    }
}