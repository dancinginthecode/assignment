package com.kakaopay.assignment.service;

import com.kakaopay.assignment.common.validation.message.ValidationMessage;
import com.kakaopay.assignment.domain.Token;
import com.kakaopay.assignment.domain.TokenDistribution;
import com.kakaopay.assignment.domain.User;
import com.kakaopay.assignment.dto.DistributionDto;
import com.kakaopay.assignment.repository.TokenRepository;
import com.kakaopay.assignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by sangwon on 20. 12. 23..
 */
@SpringBootTest
@ActiveProfiles("test")
public class ServiceDistributeTest {
    @Autowired
    DistributionService distributionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    final String prefix = "distribute.distributionDto: ";

    @BeforeEach
    public void init() {
        User user = User.builder().balance(1000000).build();
        userRepository.save(user);
    }

    /**
     * 유저가 없는 경우
     */
    @Test
    @Validated
    public void validationTest_distribute_noUser() {
        DistributionDto dto = DistributionDto.builder()
                .roomId(1L)
                .userId(999L)
                .peopleNumber(2L)
                .amount(12000L)
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.distribute(dto)
        );
        assertEquals(prefix + ValidationMessage.USER_NOT_EXIST.getMessage(), exception.getMessage());
    }

    /**
     * 사람수가 금액보다 많은경우
     */
    @Test
    @Validated
    public void validationTest_distribute_peopleOverAmount() {
        DistributionDto dto = DistributionDto.builder()
                .roomId(1L)
                .userId(1L)
                .peopleNumber(10000L)
                .amount(9999L)
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.distribute(dto)
        );
        assertEquals(prefix + ValidationMessage.PEOPLE_OVER_AMOUNT.getMessage(), exception.getMessage());
    }

    /**
     * 잔고 부족인 경우
     */
    @Test
    @Validated
    public void validationTest_distribute_notEnoughBalance() {
        DistributionDto dto = DistributionDto.builder()
                .roomId(1L)
                .userId(2L)
                .peopleNumber(5L)
                .amount(1000001L)
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.distribute(dto)
        );
        assertEquals(prefix + ValidationMessage.NOT_ENOUGH_BALANCE.getMessage(), exception.getMessage());
    }

    /**
     * 기능 테스트
     */
    @Test
    @Validated
    public void method_functional_test() {

        long userId = 1L;
        User user = userRepository.findById(userId).orElse(null);

        assertNotNull(user);
        //잔액 확인
        assertEquals(1000000L, user.getBalance());

        long peopleNumber = 5L;
        long amount = 10000L;
        DistributionDto dto = DistributionDto.builder()
                .roomId(1L)
                .userId(1L)
                .peopleNumber(peopleNumber)
                .amount(amount)
                .build();

        String tokenKey = distributionService.distribute(dto);

        user = userRepository.findById(userId).orElse(null);

        assertNotNull(user);
        //잔액 확인2
        assertEquals(1000000L - amount, user.getBalance());

        //token 길이 확인
        assertEquals(3, tokenKey.length());

        Token token = tokenRepository.findByTokenKeyAndRoomId(tokenKey, 1L).orElse(null);

        //토큰 생성 확인
        assertNotNull(token);

        //분할 확인
        assertEquals(peopleNumber, token.getTokenDistributions().size());

        //분할 합계 확인
        assertEquals(amount, token.getTokenDistributions().stream()
                .map(TokenDistribution::getAmount)
                .reduce((a, b) -> a + b).orElse(0L)
        );


    }
}