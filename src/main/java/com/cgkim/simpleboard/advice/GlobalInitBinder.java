package com.cgkim.simpleboard.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 역할: 전역 Validator, PropertyEditor 등록
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalInitBinder {

    /**
     * Validator, PropertyEditor 등록
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {

        addPropertyEditors(binder);
        addValidators(binder);
    }

    /**
     * PropertyEditor 등록
     *
     * @param binder
     */
    private void addPropertyEditors(WebDataBinder binder) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }

    /**
     * Validator 등록
     *
     * @param binder
     */
    private void addValidators(WebDataBinder binder) {

        if (binder.getTarget() == null) {
            return;
        }

        final List<Validator> validatorList = List.of();

        for (Validator validator : validatorList) {

            if (validator.supports(binder.getTarget().getClass())) {

                binder.addValidators(validator);
            }
        }
    }
}
