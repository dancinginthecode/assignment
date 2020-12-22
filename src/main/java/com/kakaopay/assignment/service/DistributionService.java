package com.kakaopay.assignment.service;

import com.kakaopay.assignment.common.exception.NotFoundException;
import com.kakaopay.assignment.domain.Token;
import com.kakaopay.assignment.domain.TokenDistribution;
import com.kakaopay.assignment.domain.User;
import com.kakaopay.assignment.dto.TokenDto;
import com.kakaopay.assignment.repository.TokenRepository;
import com.kakaopay.assignment.repository.UserRepository;
import com.kakaopay.assignment.util.RandomUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        //작으면 튕기자
        if (amount < peopleNumber) {

        }

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("유저가 존재하지 않음")
        );

        //FIXME 요구사항에 의해 잔액체크 제외됨
        user.deduct(amount);

        List<Long> distributions = RandomUtil.getRandomlyDivideAmountList(amount, peopleNumber);

        String tokenKey = RandomUtil.getRandomTokenKey();

        Token token = Token.builder()
                .roomId(roomId)
                .userId(userId)
                .tokenKey(tokenKey)
                .distributions(distributions.stream()
                        .map(TokenDistribution::new)
                        .collect(Collectors.toList()))
                .build();

        tokenRepository.save(token);

        return tokenKey;
    }


    /**
     * 2. 받기 API
     * ● 다음 조건을 만족하는 받기 API를 만들어 주세요.
     * ○ 뿌리기 시 발급된 token을 요청값으로 받습니다.
     * ○ token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를
     * API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.
     * ○ 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
     * ○ 자신이 뿌리기한 건은 자신이 받을 수 없습니다.
     * ○ 뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수
     * 있습니다.
     * ○ 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기
     * 실패 응답이 내려가야 합니다.
     */
    public void receive(long userId, long roomId, String token) {

    }

    /**
     * 3. 조회 API
     * ● 다음 조건을 만족하는 조회 API를 만들어 주세요.
     * ○ 뿌리기 시 발급된 token을 요청값으로 받습니다.
     * ○ token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재
     * 상태는 다음의 정보를 포함합니다.
     * ○ 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은
     * 사용자 아이디] 리스트)
     * ○ 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지
     * 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
     * ○ 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.
     */
    public TokenDto checkToken(long userId, long roomId, String token) {

        return null;
    }
}
