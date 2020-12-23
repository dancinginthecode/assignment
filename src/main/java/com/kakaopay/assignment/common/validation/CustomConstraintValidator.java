package com.kakaopay.assignment.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * Created by sangwon on 20. 12. 23..
 */
public class CustomConstraintValidator<T extends Annotation,T2> implements ConstraintValidator<T, T2> {

    @Override
    public boolean isValid(T2 value, ConstraintValidatorContext context) {
        return false;
    }

    protected void changeValidationMessage(ConstraintValidatorContext context, String msg) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
