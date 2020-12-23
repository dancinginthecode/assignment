package com.kakaopay.assignment.common.validation.validator;

import com.kakaopay.assignment.common.exception.ValidationException;
import com.kakaopay.assignment.common.validation.CustomConstraintValidator;
import com.kakaopay.assignment.common.validation.annotation.ValidCheckTokenDto;
import com.kakaopay.assignment.dto.TokenDto;
import com.kakaopay.assignment.repository.TokenRepository;
import com.kakaopay.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Component
@RequiredArgsConstructor
public class CheckTokenDtoValidator extends CustomConstraintValidator<ValidCheckTokenDto, TokenDto> {

    final UserRepository userRepository;
    final TokenRepository tokenRepository;

    @Override
    public void initialize(ValidCheckTokenDto validateTokenDto) {

    }

    @Override
    public boolean isValid(TokenDto tokenDto, ConstraintValidatorContext context) {

        tokenDto.setToken(tokenRepository.findByTokenKeyAndRoomId(tokenDto.getTokenKey(), tokenDto.getRoomId()).orElse(null));
        if (tokenDto.getToken() == null) {
            changeValidationMessage(context, "토큰이 존재하지 않습니다.");
            return false;
        }

        if (tokenDto.getToken().getOwnerId() != tokenDto.getUserId()) {
            changeValidationMessage(context, "본인것만 조회 가능합니다.");
            return false;
        }

        if (tokenDto.getToken().getCreatedDate().plusDays(7).compareTo(LocalDateTime.now()) < 0) {
            changeValidationMessage(context, "토큰 조회 기간이 만료되었습니다.");
            return false;
        }

        return true;
    }
}
