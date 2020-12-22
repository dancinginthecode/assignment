package com.kakaopay.assignment.dto;

import com.kakaopay.assignment.common.validation.AmountGreaterThanPeopleNumber;
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
    Long amount;

    @NotNull
    @Min(1)
    Long peopleNumber;
}
