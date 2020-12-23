package com.kakaopay.assignment.dto;

import com.kakaopay.assignment.domain.Token;
import com.kakaopay.assignment.domain.TokenDistribution;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Getter
@Builder
public class TokenResponse {
    private String distributeDate;
    private long amount;
    private long completedAmount;

    List<List<Long>> sharedInfo;

    public static TokenResponse of(Token token) {
        return TokenResponse.builder()
                .distributeDate(token.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .amount(token.getAmount())
                .completedAmount(token.getTokenDistributions().stream()
                        .filter(td -> Objects.nonNull(td.getTaker()))
                        .map(TokenDistribution::getAmount)
                        .reduce(Long::sum).orElse(0L))

                .sharedInfo(token.getTokenDistributions()
                        .stream()
                        .filter(td -> Objects.nonNull(td.getTaker()))
                        .map(td -> Arrays.asList(td.getAmount(), td.getTaker()))
                        .collect(Collectors.toList())
                )
                .build();
    }
}
