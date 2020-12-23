package com.kakaopay.assignment.common;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Builder
@Getter
public class ErrorResponse {
    private List<String> messages;

    public static ErrorResponse of(Exception ex) {
        return ErrorResponse.builder()
                .messages(Collections.singletonList(ex.getMessage()))
                .build();
    }
}
