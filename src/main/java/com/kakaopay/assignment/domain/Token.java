package com.kakaopay.assignment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Getter
@Entity
@NoArgsConstructor
@Table(name = "TOKEN")
public class Token {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private String tokenKey;

    private long roomId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "token_id")
    private List<TokenDistribution> distributions;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Builder
    public Token(String tokenKey, long roomId, List<TokenDistribution> distributions, long userId) {
        this.tokenKey = tokenKey;
        this.roomId = roomId;
        this.distributions = distributions;
    }
}
