package com.kakaopay.assignment.common.validation.validator;

import com.kakaopay.assignment.common.validation.CustomConstraintValidator;
import com.kakaopay.assignment.common.validation.annotation.ValidReceiveTokenDto;;
import com.kakaopay.assignment.common.validation.message.ValidationMessage;
import com.kakaopay.assignment.dto.TokenDto;
import com.kakaopay.assignment.repository.TokenRepository;
import com.kakaopay.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Component
@RequiredArgsConstructor
public class ReceiveTokenDtoValidator extends CustomConstraintValidator<ValidReceiveTokenDto, TokenDto> {

    final UserRepository userRepository;
    final TokenRepository tokenRepository;

    @Override
    public boolean isValid(TokenDto tokenDto, ConstraintValidatorContext context) {

        String commonValidateMessage = getCommonValidateMessage(tokenDto);
        if (Objects.nonNull(commonValidateMessage)) {
            changeValidationMessage(context, commonValidateMessage);
            return false;
        }

        tokenDto.setUser(userRepository.findById(tokenDto.getUserId()).orElse(null));
        if (tokenDto.getUser() == null) {
            changeValidationMessage(context, ValidationMessage.USER_NOT_EXIST.getMessage());
            return false;
        }

        tokenDto.setToken(tokenRepository.findByTokenKeyAndRoomId(tokenDto.getTokenKey(), tokenDto.getRoomId()).orElse(null));
        if (tokenDto.getToken() == null) {
            changeValidationMessage(context, ValidationMessage.TOKEN_NOT_EXIST.getMessage());
            return false;
        }

        if (tokenDto.getToken().getCreatedDate().plusMinutes(10).compareTo(LocalDateTime.now()) < 0) {
            changeValidationMessage(context, ValidationMessage.TOKEN_VALID_PERIOD_EXPIRED.getMessage());
            return false;
        }

        if (tokenDto.getToken().getOwnerId() == tokenDto.getUserId()) {
            changeValidationMessage(context, ValidationMessage.OWNER_CANNOT_RECEIVE.getMessage());
            return false;
        }

        if (tokenDto.getToken().getTokenDistributions().stream()
                .filter(t -> Objects.nonNull(t.getTaker()))
                .anyMatch(t -> tokenDto.getUserId() == t.getTaker())) {
            changeValidationMessage(context, ValidationMessage.ALREADY_RECEIVED.getMessage());
            return false;
        }

        tokenDto.setTokenDistribution(
                tokenDto.getToken().getTokenDistributions().stream()
                        .filter(c -> Objects.isNull(c.getTaker()))
                        .findAny().orElse(null)
        );
        if (tokenDto.getTokenDistribution() == null) {
            changeValidationMessage(context, ValidationMessage.NO_ITEMS_LEFT.getMessage());
            return false;
        }

        return true;
    }
}
