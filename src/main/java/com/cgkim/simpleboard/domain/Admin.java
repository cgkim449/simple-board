package com.cgkim.simpleboard.domain;

import lombok.AccessLevel;
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
import java.time.LocalDateTime;

/**
 * 테이블 매핑
 *  - Admin 테이블
 *
 * 연관관계 매핑
 *  - 없음
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

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

    public Admin(Long adminId,
                 String username,
                 String password,
                 String nickname,
                 LocalDateTime registerDate,
                 LocalDateTime updateDate
    ) {

        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }
}