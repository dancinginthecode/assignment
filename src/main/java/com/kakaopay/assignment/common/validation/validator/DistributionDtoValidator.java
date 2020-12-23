package com.kakaopay.assignment.common.validation.validator;

import com.kakaopay.assignment.common.validation.CustomConstraintValidator;
import com.kakaopay.assignment.common.validation.annotation.ValidDistributionDto;
import com.kakaopay.assignment.common.validation.message.ValidationMessage;
import com.kakaopay.assignment.dto.DistributionDto;
import com.kakaopay.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Component
@RequiredArgsConstructor
public class DistributionDtoValidator extends CustomConstraintValidator<ValidDistributionDto, DistributionDto> {

    final UserRepository userRepository;

    @Override
    public boolean isValid(DistributionDto distributionDto, ConstraintValidatorContext context) {

        String commonValidateMessage = getCommonValidateMessage(distributionDto);
        if (Objects.nonNull(commonValidateMessage)) {
            changeValidationMessage(context, commonValidateMessage);
            return false;
        }

        if(distributionDto.getAmount() < distributionDto.getPeopleNumber()){
            changeValidationMessage(context,ValidationMessage.PEOPLE_OVER_AMOUNT.getMessage());
            return false;
        }

        distributionDto.setUser(userRepository.findById(distributionDto.getUserId()).orElse(null));
        if(distributionDto.getUser() == null){
            changeValidationMessage(context,ValidationMessage.USER_NOT_EXIST.getMessage());
            return false;
        }

        if(distributionDto.getUser().getBalance() < distributionDto.getAmount()){
            changeValidationMessage(context,ValidationMessage.NOT_ENOUGH_BALANCE.getMessage());
            return false;
        }

        return true;
    }

}
