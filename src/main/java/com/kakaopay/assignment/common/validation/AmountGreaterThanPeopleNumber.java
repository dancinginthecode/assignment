package com.kakaopay.assignment.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Documented
@Constraint(validatedBy = AmountGreaterThanPeopleNumberValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AmountGreaterThanPeopleNumber {
    String message() default "뿌리는 금액이 인원수 보다 커야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
