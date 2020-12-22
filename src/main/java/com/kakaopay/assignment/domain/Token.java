package com.kakaopay.assignment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "token", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TokenDistribution> tokenDistributions = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Builder
    public Token(String tokenKey, long roomId, List<TokenDistribution> tokenDistributions, long userId) {
        this.createdDate = LocalDateTime.now();
        this.tokenKey = tokenKey;
        this.roomId = roomId;
        this.tokenDistributions = tokenDistributions;
    }
}
