package com.kakaopay.assignment.dto;

import com.kakaopay.assignment.domain.Token;
import com.kakaopay.assignment.domain.TokenDistribution;
import com.kakaopay.assignment.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by sangwon on 20. 12. 23..
 */
@Getter
@Builder
public class TokenDto {
    @NotNull
    Long userId;

    @NotNull
    Long roomId;

    @Pattern(regexp = "^[A-Za-z]{3}$")
    String tokenKey;

    @Setter
    User user;

    @Setter
    Token token;

    @Setter
    TokenDistribution tokenDistribution;
}
