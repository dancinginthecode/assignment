package com.kakaopay.assignment.service;

import com.kakaopay.assignment.domain.User;
import com.kakaopay.assignment.repository.TokenRepository;
import com.kakaopay.assignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.BDDAssertions.then;
/**
 * Created by sangwon on 20. 12. 23..
 */
@SpringBootTest
@ActiveProfiles("test")
public class ServiceTest {
    @Autowired
    DistributionService distributionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @BeforeEach
    public void init(){
        User user = User.builder().balance(1000000).build();
        userRepository.save(user);
    }
}
