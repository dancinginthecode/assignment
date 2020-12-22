package com.kakaopay.assignment.dto;

import lombok.Builder;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Builder
public class TokenResponse {
    String distributeDate;
    long amount;
    long completedAmount;

    List<List<Long>> sharedInfo;

    public static TokenResponse of (TokenDto tokenDto){
        return TokenResponse.builder()
                .distributeDate(tokenDto.distributeDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .amount(tokenDto.amount)
                .completedAmount(tokenDto.completedAmount)
                .sharedInfo(null)
                .build();
    }
}
