package com.cgkim.simpleboard.domain;

import com.cgkim.simpleboard.util.AttachURIProvider;
import com.cgkim.simpleboard.util.SHA256PasswordEncoder;
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

/**
 * 테이블 매핑
 *  - Board 테이블
 *
 * 연관관계 매핑
 *  - 다대일 : Category, Member, Admin
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    /**
     * OneToMany : 단순히 객체 그래프 탐색 및 JPQL 사용이 목적
     *
     * CascadeType.ALL
     *  - Board 를 db 에 삽입, 삭제 할때 Attach 도 함께 db 에 삽입, 삭제 된다
     *      - 단 Attach 리스트에서 Attach 를 제거한다고 Attach 가 db 에서 삭제되지는 않는다. 단순히 참조를 제거한거지 Attach 자체를 제거한 건 아니기 때문이다.
     *          - orphanRemoval 을 이용하면 가능해진다
     *  - Attach 를 참조하는 객체가 Board 밖에 없어서 안전하다
     *  - Board 와 Attach 의 생명주기가 같기 때문에 편리하다
     *
     * orphanRemoval = true
     *  - 컬렉션에서 제거하면 db 에도 반영된다
     */
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Attach> attaches = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
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

    //TODO: 상속
    @CreatedDate
    private Date registerDate;

    @LastModifiedDate
    private Date updateDate;

    @Builder
    private Board(Long boardId,
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
     * 역할
     *  - Board 엔티티 생성 (익명 게시물)
     *
     * 목적
     *  - 엔티티 생성 로직을 한 곳에서 관리
     *      - 다른 생성자는 호출 못하게 private 나 protected 로 변경
     *
     * @param category
     * @param insertAttaches
     * @param title
     * @param content
     * @param guestNickname
     * @param guestPassword
     * @return
     * @throws NoSuchAlgorithmException
     */
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

        if (insertAttaches != null) {
            for (Attach attach : insertAttaches) {
                board.addAttach(attach);
            }
        }

        return board;
    }

    /**
     * Board 엔티티 생성 (회원 게시물)
     *
     * @param member
     * @param category
     * @param insertAttaches
     * @param title
     * @param content
     * @return
     */
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

        if (insertAttaches != null) {
            for (Attach attach : insertAttaches) {
                board.addAttach(attach);
            }
        }

        return board;
    }

    /**
     * 역할
     *  - Board 에 Member 넣어주고, Member 에도 Board 넣어준다
     *
     * 목적
     *  - 누락 방지
     *
     * @param member
     */
    public void setMember(Member member) {

        if (this.member != null) { //기존에 참조 값이 있다면 제거한다
            this.member.getBoards().remove(this);
        }

        this.member = member;

        if(!member.getBoards().contains(this)) { //재귀호출 방지
            member.getBoards().add(this);
        }
    }

    /**
     * 위와 같음
     *
     * @param category
     */
    public void setCategory(Category category) {

        if (this.category != null) {
            this.category.getBoards().remove(this);
        }

        this.category = category;

        if(!category.getBoards().contains(this)) {
            category.getBoards().add(this);
        }
    }

    /**
     * 위와 같음
     *
     * @param attach
     */
    public void addAttach(Attach attach) {
        attaches.add(attach);
        attach.setBoard(this);
    }


    //==비즈니스 로직==//

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

        //TODO: 동시성 문제
        for (Long attachId : attachDeleteRequest) {
            while (iterator.hasNext()) {
                Attach attach = iterator.next();

                if (Objects.equals(attach.getAttachId(), attachId)) {
                    iterator.remove();
                }
            }
        }

        if (insertAttaches != null) {
            for (Attach attach : insertAttaches) {
                addAttach(attach);
            }
        }
    }

    /**
     * 첨부파일 유무 업데이트
     */
    public void updateHasAttach() {
        if (attaches.size() > 0) {
            hasAttach = true;
        } else {
            hasAttach = false;
        }
    }

    /**
     * 썸네일 URI 업데이트
     */
    public void updateThumbnailUri() {
        if (hasAttach) {
            for (Attach attach : attaches) {
                if (attach.getIsImage()) {

                    this.thumbnailUri = AttachURIProvider.createThumbnailURIForDB(attach.getUploadPath(), attach.getUuid(), attach.getExtension());
                    return;
                }
            }
        }

        this.thumbnailUri = "";
    }

    /**
     * 조회수 증가
     */
    public void increaseViewCount() {
        viewCount++;
    }

    /**
     * 익명 글 비밀번호 검증
     *
     * @param guestPassword
     * @return
     * @throws NoSuchAlgorithmException
     */
    public boolean isPasswordMismatch(String guestPassword) throws NoSuchAlgorithmException {
        return !this.guestPassword.equals(SHA256PasswordEncoder.getHash(guestPassword));
    }
}