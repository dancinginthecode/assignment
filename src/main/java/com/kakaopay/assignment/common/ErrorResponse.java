package com.kakaopay.assignment.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Builder
@Getter
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private List<String> errors;
}
