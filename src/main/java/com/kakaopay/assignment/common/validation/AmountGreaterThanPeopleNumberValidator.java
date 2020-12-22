package com.kakaopay.assignment.common.validation;

import com.kakaopay.assignment.dto.DistributionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Component
@RequiredArgsConstructor
public class AmountGreaterThanPeopleNumberValidator implements ConstraintValidator<AmountGreaterThanPeopleNumber, DistributionRequest> {

    @Override
    public void initialize(AmountGreaterThanPeopleNumber amountGreaterThanPeopleNumber) {

    }

    @Override
    public boolean isValid(DistributionRequest request, ConstraintValidatorContext context) {
        return request.getAmount() > request.getPeopleNumber();
    }

}
