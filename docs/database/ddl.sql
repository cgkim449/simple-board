-- TODO: pk, constraint 등을 alter table 로 수정

CREATE TABLE admin
(
    admin_id      INT AUTO_INCREMENT COMMENT '관리자 ID'
        PRIMARY KEY,
    username      VARCHAR(30)                           NOT NULL COMMENT '이메일',
    password      CHAR(64)                              NOT NULL COMMENT '비밀번호',
    nickname      VARCHAR(30)                           NOT NULL COMMENT '별명',
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT admin_nickname_uindex
        UNIQUE (nickname),
    CONSTRAINT admin_username_uindex
        UNIQUE (username)
)
    COMMENT '관리자';

CREATE TABLE category
(
    category_id   INT                                   NOT NULL COMMENT '카테고리 ID',
    name          VARCHAR(30)                           NOT NULL COMMENT '이름',
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    PRIMARY KEY (category_id)
)
    COMMENT '카테고리';

CREATE TABLE member
(
    member_id     INT AUTO_INCREMENT COMMENT '회원 ID'
        PRIMARY KEY,
    username      VARCHAR(30)                           NOT NULL COMMENT '이메일',
    password      CHAR(64)                              NOT NULL COMMENT '비밀번호',
    nickname      VARCHAR(30)                           NOT NULL COMMENT '별명',
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT member_nickname_uindex
        UNIQUE (nickname),
    CONSTRAINT member_username_uindex
        UNIQUE (username)
)
    COMMENT '회원';


CREATE TABLE board
(
    board_id       INT AUTO_INCREMENT COMMENT '게시물 ID'
        PRIMARY KEY,
    category_id    INT                                    NOT NULL COMMENT '카테고리 ID',
    member_id      INT                                    NULL COMMENT '회원 ID',
    admin_id       INT                                    NULL COMMENT '관리자 ID',
    guest_nickname VARCHAR(30)                            NULL COMMENT '익명 게시자 별명',
    guest_password CHAR(64)                               NULL COMMENT '익명 게시자 비밀번호',
    title          VARCHAR(100)                           NOT NULL COMMENT '제목',
    content        TEXT                                   NOT NULL COMMENT '내용',
    view_count     INT        DEFAULT 0                   NULL COMMENT '조회수',
    has_attach     TINYINT(1) DEFAULT 0                   NULL COMMENT '첨부파일 유무',
    thumbnail_uri  VARCHAR(100)                           NULL COMMENT '썸네일 URI',
    register_date  TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date    TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT board_category_category_id_fk
        FOREIGN KEY (category_id) REFERENCES category (category_id),
    CONSTRAINT board_admin_admin_id_fk
        FOREIGN KEY (admin_id) REFERENCES admin (admin_id),
    CONSTRAINT board_member_member_id_fk
        FOREIGN KEY (member_id) REFERENCES member (member_id)
)
    COMMENT '자유게시판';

CREATE TABLE attach
(
    attach_id     INT AUTO_INCREMENT COMMENT '첨부파일 ID'
        PRIMARY KEY,
    board_id      INT                                    NOT NULL COMMENT '게시글 ID',
    upload_path   VARCHAR(200)                           NOT NULL COMMENT '저장 경로',
    uuid          VARCHAR(100)                           NOT NULL COMMENT 'UUID',
    name          VARCHAR(100)                           NOT NULL COMMENT '이름',
    extension     VARCHAR(20)                            NOT NULL COMMENT '확장자',
    is_image      TINYINT(1) DEFAULT 0                   NULL COMMENT '이미지 여부',
    size          INT                                    NOT NULL COMMENT '파일 크기',
    register_date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT attach_board_board_id_fk
        FOREIGN KEY (board_id) REFERENCES board (board_id)
)
    COMMENT '자유게시판 첨부파일';

CREATE TABLE comment
(
    comment_id     INT AUTO_INCREMENT COMMENT '댓글 ID'
        PRIMARY KEY,
    board_id       INT                                   NOT NULL COMMENT '게시글 ID',
    member_id      INT                                   NULL COMMENT '회원 ID',
    admin_id       INT                                   NULL COMMENT '관리자 ID',
    guest_nickname VARCHAR(30)                           NULL COMMENT '익명 댓글 작성자 별명',
    guest_password CHAR(64)                              NULL COMMENT '익명 댓글 작성자 비밀번호',
    content        VARCHAR(1000)                         NOT NULL COMMENT '내용',
    register_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '등록일',
    update_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NULL COMMENT '수정일',
    CONSTRAINT comment_admin_admin_id_fk
        FOREIGN KEY (admin_id) REFERENCES admin (admin_id),
    CONSTRAINT comment_board_board_id_fk
        FOREIGN KEY (board_id) REFERENCES board (board_id),
    CONSTRAINT comment_member_member_id_fk
        FOREIGN KEY (member_id) REFERENCES member (member_id)
)
    COMMENT '자유게시판 댓글';
