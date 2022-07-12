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

    /**
     * nullable =  true 면 아우터 조인.
     * 이너 조인이 더 성능과 최적화면에서 좋다. 근데 lazy 에서도 되나
     */
    //TODO: lazy eager 정리. 자주 함께 사용되면 eager. 가끔 사용되면 lazy?. 처음에 한번에 다가져오는게 더 좋을 수도 잇음
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", nullable = false)
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
     * CascadeType.ALL : Board 를 persist/update 할때 자동으로 함께 persist/update 된다 (단, delete 는 안됨)
     *  - Attach 를 참조하는 엔티티가 Board 밖에 없고, Board 와 Attach 가 persist 되는 시점이 갖기 때문에 사용
     *
     *  orphanRemoval = true : 고아 객체 db 삭제
     */
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
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

    //TODO: 기본 생성자를 protected 로해서. 왜 protected?
    /**
     * - 역할
     *   - Board 엔티티 생성
     * - 목적
     *   - 일관된 엔티티 생성
     *   - 엔티티 생성 로직 변경 시 여기만 변경하면 됨
     *   - 기본생성자는 사용 못하게 protected 로
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

    public void setMember(Member member) {

        if (this.member != null) {
            this.member.getBoards().remove(this);
        }

        this.member = member;

        if(!member.getBoards().contains(this)) {
            member.getBoards().add(this);
        }
    }

    public void setCategory(Category category) {

        if (this.category != null) {
            this.category.getBoards().remove(this);
        }

        this.category = category;

        if(!category.getBoards().contains(this)) {
            category.getBoards().add(this);
        }
    }

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