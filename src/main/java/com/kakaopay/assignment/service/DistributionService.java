package com.kakaopay.assignment.service;

import com.kakaopay.assignment.common.exception.NotFoundException;
import com.kakaopay.assignment.common.exception.ValidationException;
import com.kakaopay.assignment.domain.Token;
import com.kakaopay.assignment.domain.User;
import com.kakaopay.assignment.repository.TokenRepository;
import com.kakaopay.assignment.repository.UserRepository;
import com.kakaopay.assignment.util.RandomUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Service
public class DistributionService {
    final UserRepository userRepository;
    final TokenRepository tokenRepository;

    public DistributionService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public String distribute(long userId, long roomId, long amount, long peopleNumber) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("유저가 존재하지 않음")
        );

        //FIXME 요구사항에 의해 잔액체크 제외됨
        user.deductAmount(amount);

        List<Long> distributions = RandomUtil.getRandomlyDivideAmountList(amount, peopleNumber);

        String tokenKey = RandomUtil.getRandomTokenKey();

        Token token = Token.builder()
                .amount(amount)
                .roomId(roomId)
                .ownerId(userId)
                .tokenKey(tokenKey)
                .tokenDistributions(distributions)
                .build();

        tokenRepository.save(token);

        return tokenKey;
    }

    @Transactional
    public long receive(long userId, long roomId, String tokenKey) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("유저가 존재하지 않음")
        );

        Token token = tokenRepository.findByTokenKeyAndRoomId(tokenKey, roomId).orElseThrow(() ->
                new NotFoundException("토큰이 존재하지 않음")
        );

        if (token.getCreatedDate().compareTo(LocalDateTime.now().plusMinutes(10)) > 0) {
            throw new ValidationException("토큰이 만료됨");
        }

        if (token.getOwnerId() == userId) {
            throw new ValidationException("본인 공유 사용불가");
        }

        if (token.getTokenDistributions().stream().filter(t -> Objects.nonNull(t.getTaker())).anyMatch(t -> userId == t.getTaker())) {
            throw new ValidationException("1회 이상 사용 불가");
        }

        long amount = token.distribute(userId);

        user.addAmount(amount);

        return amount;
    }

    @Transactional(readOnly = true)
    public Token checkToken(long userId, long roomId, String tokenKey) {
        Token token = tokenRepository.findByTokenKeyAndRoomIdAndOwnerId(tokenKey, roomId, userId)
                .orElseThrow(() -> new NotFoundException("토큰이 존재하지 않음"));

        if (token.getCreatedDate().compareTo(LocalDateTime.now().plusDays(7)) > 0) {
            throw new ValidationException("토큰이 만료됨");
        }
        return token;
    }
}
