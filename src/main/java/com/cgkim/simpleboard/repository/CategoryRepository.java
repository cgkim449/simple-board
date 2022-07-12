package com.cgkim.simpleboard.repository;

import com.cgkim.simpleboard.domain.Category;
import com.cgkim.simpleboard.exception.AttachNotFoundException;
import com.cgkim.simpleboard.exception.CategoryNotFoundException;
import com.cgkim.simpleboard.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class CategoryRepository {

    private final EntityManager em;

    /**
     * categoryId 로 게시물 조회
     *
     * @param categoryId
     * @return
     */
    public Category findByCategoryId(Long categoryId) {

        Category category = em.find(Category.class, categoryId);

        if (category == null) {
            throw new CategoryNotFoundException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        return category;
    }
}