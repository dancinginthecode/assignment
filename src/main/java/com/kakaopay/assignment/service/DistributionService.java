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
        //작으면 튕기자, 시간이 남으면 dto에 구현
        if (amount < peopleNumber) {
            throw new ValidationException("뿌리는 금액이 인원수 보다 커야 합니다.");
        }

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


    /**
     * 2. 받기 API
     */
    @Transactional
    public long receive(long userId, long roomId, String tokenKey) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("유저가 존재하지 않음")
        );

        //뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.
        Token token = tokenRepository.findByTokenKeyAndRoomId(tokenKey, roomId).orElseThrow(() ->
                new NotFoundException("토큰이 존재하지 않음"));

        //뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.
        if (token.getCreatedDate().compareTo(LocalDateTime.now().plusMinutes(10)) > 0) {
            throw new ValidationException("토큰 만료");
        }

        //자신이 뿌리기한 건은 자신이 받을 수 없습니다.
        if (token.getOwnerId() == userId) {
            throw new ValidationException("자신이 뿌리기한 건은 자신이 받을 수 없습니다.");
        }

        if (token.getTokenDistributions().stream().filter(t-> Objects.nonNull(t.getTaker())).anyMatch(t -> userId == t.getTaker())) {
            throw new ValidationException("뿌리기 당 한 사용자는 한번만 받을 수 있습니다.");
        }

        long amount = token.distribute(userId);

        user.addAmount(amount);
        return amount;
    }

    public Token checkToken(long userId, long roomId, String tokenKey) {
        //○ 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
        Token token = tokenRepository.findByTokenKeyAndRoomIdAndOwnerId(tokenKey, roomId, userId).orElseThrow(() ->
                new NotFoundException("토큰이 존재하지 않음"));

        //뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.
        if (token.getCreatedDate().compareTo(LocalDateTime.now().plusDays(7)) > 0) {
            throw new ValidationException("토큰 만료");
        }
        return token;
    }
}
