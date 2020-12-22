package com.kakaopay.assignment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Getter
@Entity
@NoArgsConstructor
@Table(name = "TOKEN_DISTRIBUTION")
public class TokenDistribution {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "token_id")
    private Token token;

    private Long taker;

    private long amount;

    @Builder
    public TokenDistribution(Token token, long amount) {
        this.token = token;
        this.amount = amount;
    }

    public void use(long taker) {
        this.taker = taker;
    }

    public static TokenDistribution of(Token token, long amount) {
        return TokenDistribution.builder()
                .token(token)
                .amount(amount)
                .build();
    }
}
