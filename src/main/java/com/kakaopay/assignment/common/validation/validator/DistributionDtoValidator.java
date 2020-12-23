package com.kakaopay.assignment.common.validation.validator;

import com.kakaopay.assignment.common.validation.CustomConstraintValidator;
import com.kakaopay.assignment.common.validation.annotation.ValidDistributionDto;
import com.kakaopay.assignment.dto.DistributionDto;
import com.kakaopay.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidatorContext;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Component
@RequiredArgsConstructor
public class DistributionDtoValidator extends CustomConstraintValidator<ValidDistributionDto, DistributionDto> {

    final UserRepository userRepository;

    @Override
    public void initialize(ValidDistributionDto validateDistributionDto) {

    }

    @Override
    public boolean isValid(DistributionDto distributionDto, ConstraintValidatorContext context) {

        if(distributionDto.getAmount() < distributionDto.getPeopleNumber()){
            changeValidationMessage(context,"금액이 인원수 보다 커야 합니다.");
            return false;
        }

        distributionDto.setUser(userRepository.findById(distributionDto.getUserId()).orElse(null));
        if(distributionDto.getUser() == null){
            changeValidationMessage(context,"유저가 존재하지 않습니다.");
            return false;
        }

        if(distributionDto.getUser().getBalance() < distributionDto.getAmount()){
            changeValidationMessage(context,"잔액이 부족합니다.");
            return false;
        }

        return true;
    }

}
