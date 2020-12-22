package com.kakaopay.assignment.common;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Builder
@Getter
public class ApiResponse {
    private Object body;

    public static ApiResponse of(Object body) {
        return ApiResponse.builder().body(body).build();
    }
}
