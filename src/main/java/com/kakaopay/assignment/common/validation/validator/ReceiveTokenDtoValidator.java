package com.kakaopay.assignment.common.validation.validator;

import com.kakaopay.assignment.common.validation.CustomConstraintValidator;
import com.kakaopay.assignment.common.validation.annotation.ValidReceiveTokenDto;;
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
    public void initialize(ValidReceiveTokenDto validateTokenDto) {

    }

    @Override
    public boolean isValid(TokenDto tokenDto, ConstraintValidatorContext context) {

        tokenDto.setUser(userRepository.findById(tokenDto.getUserId()).orElse(null));
        if (tokenDto.getUser() == null) {
            changeValidationMessage(context, "유저가 존재하지 않습니다.");
            return false;
        }

        tokenDto.setToken(tokenRepository.findByTokenKeyAndRoomId(tokenDto.getTokenKey(), tokenDto.getRoomId()).orElse(null));
        if (tokenDto.getToken() == null) {
            changeValidationMessage(context, "토큰이 존재하지 않습니다.");
            return false;
        }

        if (tokenDto.getToken().getCreatedDate().plusMinutes(10).compareTo(LocalDateTime.now()) < 0) {
            changeValidationMessage(context, "토큰 유효기간이 만료되었습니다.");
            return false;
        }

        if (tokenDto.getToken().getOwnerId() == tokenDto.getUserId()) {
            changeValidationMessage(context, "본인이 공유한 항목은 받을 수 없습니다.");
            return false;
        }

        if (tokenDto.getToken().getTokenDistributions().stream()
                .filter(t -> Objects.nonNull(t.getTaker()))
                .anyMatch(t -> tokenDto.getUserId() == t.getTaker())) {
            changeValidationMessage(context, "이미 받은 항목입니다.");
            return false;
        }

        tokenDto.setTokenDistribution(
                tokenDto.getToken().getTokenDistributions().stream()
                        .filter(c -> Objects.isNull(c.getTaker()))
                        .findAny().orElse(null)
        );
        if (tokenDto.getTokenDistribution() == null) {
            changeValidationMessage(context, "모두 받은 항목입니다.");
            return false;
        }

        return true;
    }
}
