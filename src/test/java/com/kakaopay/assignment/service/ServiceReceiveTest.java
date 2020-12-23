package com.kakaopay.assignment.service;

import com.kakaopay.assignment.common.validation.message.ValidationMessage;
import com.kakaopay.assignment.common.validation.validator.DistributionDtoValidator;
import com.kakaopay.assignment.domain.Token;
import com.kakaopay.assignment.domain.User;
import com.kakaopay.assignment.dto.DistributionDto;
import com.kakaopay.assignment.dto.TokenDto;
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
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by sangwon on 20. 12. 23..
 */
@SpringBootTest
@ActiveProfiles("test")
public class ServiceReceiveTest {
    @Autowired
    DistributionService distributionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    final String prefix = "receive.tokenDto: ";

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
     * 유저가 없는 경우
     */
    @Test
    @Validated
    public void validationTest_receive_noUser() {
        TokenDto dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(9999L)
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.receive(dto)
        );
        assertEquals(prefix + ValidationMessage.USER_NOT_EXIST.getMessage(), exception.getMessage());
    }

    /**
     * 토큰 없는 경우
     */
    @Test
    @Validated
    public void validationTest_receive_noToken() {
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
                distributionService.receive(dto)
        );
        assertEquals(prefix + ValidationMessage.TOKEN_NOT_EXIST.getMessage(), exception.getMessage());
    }

    /**
     * 다른방일 경우, 조회조건이 토큰 & 방이므로 조회되지 않음
     */
    @Test
    @Validated
    public void validationTest_receive_diff_room() {
        TokenDto dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(9999L)
                .userId(1L)
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.receive(dto)
        );
        assertEquals(prefix + ValidationMessage.TOKEN_NOT_EXIST.getMessage(), exception.getMessage());
    }

    /**
     * 받는 기간 만료시
     */
    @Test
    @Validated
    public void validationTest_receive_valid_period_expired() {
        TokenDto dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(2L)
                .build();

        //10분 전으로 시간 조정
        Token token = tokenRepository.findByTokenKeyAndRoomId(sampleTokenKey, 1L).orElse(null);
        ReflectionTestUtils.setField(token, "createdDate", LocalDateTime.now().minusMinutes(11));
        tokenRepository.saveAndFlush(token);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.receive(dto)
        );
        assertEquals(prefix + ValidationMessage.TOKEN_VALID_PERIOD_EXPIRED.getMessage(), exception.getMessage());
    }

    /**
     * 생성자가 받을려고 할 경우
     */
    @Test
    @Validated
    public void validationTest_receive_owner_cannot() {
        TokenDto dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(1L)
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.receive(dto)
        );
        assertEquals(prefix + ValidationMessage.OWNER_CANNOT_RECEIVE.getMessage(), exception.getMessage());
    }

    /**
     * 이미 받은 경우
     */
    @Test
    @Validated
    public void validationTest_receive_already_receive() {
        TokenDto dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(2L)
                .build();

        distributionService.receive(dto);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.receive(dto)
        );
        assertEquals(prefix + ValidationMessage.ALREADY_RECEIVED.getMessage(), exception.getMessage());
    }

    /**
     * 모두 받아진 경우
     */
    @Test
    @Validated
    public void validationTest_receive_no_left() {
        for (long i = 2; i <= 4; i++) {
            TokenDto dto = TokenDto.builder()
                    .tokenKey(sampleTokenKey)
                    .roomId(1L)
                    .userId(i)
                    .build();
            distributionService.receive(dto);
        }
        TokenDto dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(5L)
                .build();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () ->
                distributionService.receive(dto)
        );

        assertEquals(prefix + ValidationMessage.NO_ITEMS_LEFT.getMessage(), exception.getMessage());
    }

    /**
     * 기능 테스트
     */
    @Test
    @Validated
    public void method_functional_test() {
        long userId = 2L;

        User user = userRepository.findById(userId).orElse(null);

        assertNotNull(user);
        //현재 잔액
        long currentBalance = user.getBalance();

        TokenDto dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(2L)
                .build();

        long earned1 = distributionService.receive(dto);

        user = userRepository.findById(userId).orElse(null);

        assertNotNull(user);

        //획득 잔액 확인
        assertEquals(currentBalance + earned1, user.getBalance());

        dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(3L)
                .build();
        long earned2 = distributionService.receive(dto);

        dto = TokenDto.builder()
                .tokenKey(sampleTokenKey)
                .roomId(1L)
                .userId(4L)
                .build();
        long earned3 = distributionService.receive(dto);

        //각 획득 총합 확인
        assertEquals(100000L, earned1 + earned2 + earned3);
    }
}