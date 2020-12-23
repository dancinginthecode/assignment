package com.kakaopay.assignment.service;

import com.kakaopay.assignment.common.validation.message.ValidationMessage;
import com.kakaopay.assignment.domain.Token;
import com.kakaopay.assignment.domain.User;
import com.kakaopay.assignment.dto.DistributionDto;
import com.kakaopay.assignment.dto.TokenDto;
import com.kakaopay.assignment.dto.TokenResponse;
import com.kakaopay.assignment.repository.TokenRepository;
import com.kakaopay.assignment.repository.UserRepository;
import com.kakaopay.assignment.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by sangwon on 20. 12. 23..
 */
@SpringBootTest
@ActiveProfiles("test")
public class ServiceCheckTest {
    @Autowired
    DistributionService distributionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    final String prefix = "checkToken.tokenDto: ";

    String sampleTokenKey;

    @BeforeEach
    public void init() {
        for (int i = 0; i < 5; i++) {
            User user = User.builder().balance(1000000).build();
            userRepository.save(user);
        }

        DistributionDto dto = DistributionDto.builder()
                .roomId(1L)
                .userId(1L)
                .peopleNumber(3L)
                .amount(100000L)
                .build();

        sampleTokenKey = distributionService.distribute(dto);
    }

    /**
     * 토큰이 없는 경우
     */
    @Test
    @Validated
    public void validationTest_check_noToken() {
        String diff = RandomUtil.getRandomTokenKey(3);
        while (diff.equals(sampleTokenKey)) {
            diff = RandomUtil.getRandomTokenKey(3);
        }
        TokenDto dto = TokenDto.builder()
                .tokenKey(diff)
                .roomId(1L)
                .userId(1L)
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.checkToken(dto)
        );
        assertEquals(prefix + ValidationMessage.TOKEN_NOT_EXIST.getMessage(), exception.getMessage());
    }

    /**
     * 생성자가 아닌경우
     */
    @Test
    @Validated
    public void validationTest_check_not_owner() {
        TokenDto dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(2L)
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.checkToken(dto)
        );
        assertEquals(prefix + ValidationMessage.HAS_TO_BE_OWNER.getMessage(), exception.getMessage());
    }

    /**
     * 조회기간 만료
     */
    @Test
    @Validated
    public void validationTest_check_search_period_expire() {
        TokenDto dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(1L)
                .build();

        //7일 전으로 시간 조정
        Token token = tokenRepository.findByTokenKeyAndRoomId(sampleTokenKey, 1L).orElse(null);
        ReflectionTestUtils.setField(token, "createdDate", LocalDateTime.now().minusDays(7));
        tokenRepository.saveAndFlush(token);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.checkToken(dto)
        );
        assertEquals(prefix + ValidationMessage.TOKEN_SEARCH_PERIOD_EXPIRED.getMessage(), exception.getMessage());
    }

    /**
     * 기능 테스트
     */
    @Test
    @Validated
    public void method_functional_test() {
        TokenDto dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(1L)
                .build();

        Token token = distributionService.checkToken(dto);
        TokenResponse response = TokenResponse.of(token);
        //초기값 확인
        assertEquals(Collections.emptyList(), response.getSharedInfo());
        assertEquals(100000L, response.getAmount());
        assertEquals(0L, response.getCompletedAmount());

        TokenDto dto2 = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(2L)
                .build();
        long earned = distributionService.receive(dto2);

        token = distributionService.checkToken(dto);
        response = TokenResponse.of(token);

        //변경된 값 확인
        assertEquals(earned, response.getCompletedAmount());
        assertEquals(Collections.singletonList(Arrays.asList(earned, 2L)), response.getSharedInfo());
    }
}