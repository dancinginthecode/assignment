package com.kakaopay.assignment.dto;

import lombok.Getter;

import javax.validation.constraints.Min;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Getter
public class DistributionRequest {
    @Min(1)
    long amount;
    @Min(1)
    long peopleNumber;
}
