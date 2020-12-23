package com.kakaopay.assignment.dto;

import com.kakaopay.assignment.domain.Token;
import com.kakaopay.assignment.domain.User;
import com.kakaopay.assignment.util.RandomUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by sangwon on 20. 12. 23..
 */
@Getter
@Builder
public class DistributionDto {
    @NotNull
    Long userId;

    @NotNull
    Long roomId;

    @NotNull
    @Min(1)
    private Long amount;

    @NotNull
    @Min(1)
    private Long peopleNumber;

    @Setter
    private User user;

    public Token toToken(){
        return Token.builder()
                .amount(amount)
                .roomId(roomId)
                .ownerId(userId)
                .tokenKey(RandomUtil.getRandomTokenKey(3))
                .tokenDistributions(RandomUtil.getRandomlyDividedAmountList(amount, peopleNumber))
                .build();
    }
}
