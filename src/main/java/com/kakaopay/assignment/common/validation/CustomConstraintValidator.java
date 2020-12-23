package com.kakaopay.assignment.common.validation;

import com.kakaopay.assignment.dto.TokenDto;

import javax.validation.*;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by sangwon on 20. 12. 23..
 */
public class CustomConstraintValidator<T extends Annotation, T2> implements ConstraintValidator<T, T2> {
    protected ValidatorFactory validatorFactory;
    protected Validator validator;

    public CustomConstraintValidator(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public boolean isValid(T2 value, ConstraintValidatorContext context) {
        return false;
    }

    @Override
    public void initialize(T constraintAnnotation) { }

    public String getCommonValidateMessage(T2 dto) {
        Set<ConstraintViolation<T2>> violations = validator.validate(dto);
        if (violations.isEmpty()) {
            return null;
        } else {
            return violations.stream()
                    .map(c -> c.getPropertyPath() + ":" + c.getMessage())
                    .collect(Collectors.joining("\n"));
        }
    }

    protected void changeValidationMessage(ConstraintValidatorContext context, String msg) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
