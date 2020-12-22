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

    private long tokenId;

    private boolean available;

    private long amount;

    @Builder
    public TokenDistribution(long amount) {
        this.available = true;
        this.amount = amount;
    }

    public static TokenDistribution of(long amount) {
        return TokenDistribution.builder().amount(amount).build();
    }
}
