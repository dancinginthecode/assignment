package com.kakaopay.assignment.common.validation.validator;

import com.kakaopay.assignment.common.validation.CustomConstraintValidator;
import com.kakaopay.assignment.common.validation.annotation.ValidCheckTokenDto;
import com.kakaopay.assignment.common.validation.message.ValidationMessage;
import com.kakaopay.assignment.dto.TokenDto;
import com.kakaopay.assignment.repository.TokenRepository;
import com.kakaopay.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Component
@RequiredArgsConstructor
public class CheckTokenDtoValidator extends CustomConstraintValidator<ValidCheckTokenDto, TokenDto> {

    final UserRepository userRepository;
    final TokenRepository tokenRepository;

    @Override
    public boolean isValid(TokenDto tokenDto, ConstraintValidatorContext context) {

        String commonValidateMessage = getCommonValidateMessage(tokenDto);
        if (Objects.nonNull(commonValidateMessage)) {
            changeValidationMessage(context, commonValidateMessage);
            return false;
        }

        tokenDto.setToken(tokenRepository.findByTokenKeyAndRoomId(tokenDto.getTokenKey(), tokenDto.getRoomId()).orElse(null));
        if (tokenDto.getToken() == null) {
            changeValidationMessage(context, ValidationMessage.TOKEN_NOT_EXIST.getMessage());
            return false;
        }

        if (tokenDto.getToken().getOwnerId() != tokenDto.getUserId()) {
            changeValidationMessage(context, ValidationMessage.HAS_TO_BE_OWNER.getMessage());
            return false;
        }

        if (tokenDto.getToken().getCreatedDate().plusDays(7).compareTo(LocalDateTime.now()) < 0) {
            changeValidationMessage(context, ValidationMessage.TOKEN_SEARCH_PERIOD_EXPIRED.getMessage());
            return false;
        }

        return true;
    }
}
