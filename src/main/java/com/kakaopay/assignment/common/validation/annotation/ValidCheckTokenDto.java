package com.kakaopay.assignment.common.validation.annotation;

import com.kakaopay.assignment.common.validation.validator.CheckTokenDtoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Documented
@Constraint(validatedBy = CheckTokenDtoValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCheckTokenDto {
    String message() default "요청이 거부되었습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
