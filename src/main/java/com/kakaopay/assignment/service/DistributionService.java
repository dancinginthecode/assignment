package com.kakaopay.assignment.service;

import com.kakaopay.assignment.common.validation.annotation.ValidCheckTokenDto;
import com.kakaopay.assignment.common.validation.annotation.ValidDistributionDto;
import com.kakaopay.assignment.common.validation.annotation.ValidReceiveTokenDto;
import com.kakaopay.assignment.domain.Token;
import com.kakaopay.assignment.dto.DistributionDto;
import com.kakaopay.assignment.dto.TokenDto;
import com.kakaopay.assignment.repository.TokenRepository;
import com.kakaopay.assignment.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Validated
@Service
public class DistributionService {
    final private UserRepository userRepository;
    final private TokenRepository tokenRepository;

    public DistributionService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public String distribute(@ValidDistributionDto DistributionDto distributionDto) {
        distributionDto.getUser()
                .deductAmount(distributionDto.getAmount());

        Token token = distributionDto
                .toToken();

        tokenRepository.save(token);

        return token.getTokenKey();
    }

    @Transactional
    public long receive(@ValidReceiveTokenDto TokenDto tokenDto) {
        tokenDto.getTokenDistribution()
                .use(tokenDto.getUserId());

        long amount = tokenDto.getTokenDistribution().getAmount();

        tokenDto.getUser()
                .addAmount(amount);

        return amount;
    }

    @Transactional(readOnly = true)
    public Token checkToken(@ValidCheckTokenDto TokenDto tokenDto) {
        return tokenDto.getToken();
    }
}
