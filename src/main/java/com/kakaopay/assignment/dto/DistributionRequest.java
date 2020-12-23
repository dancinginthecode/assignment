package com.kakaopay.assignment.dto;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Getter
public class DistributionRequest {
    @NotNull
    @Min(1)
    private Long amount;

    @NotNull
    @Min(1)
    private Long peopleNumber;
}
