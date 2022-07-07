package com.cgkim.simpleboard.repository;

import com.cgkim.simpleboard.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}