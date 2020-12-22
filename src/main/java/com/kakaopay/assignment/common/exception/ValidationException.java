package com.kakaopay.assignment.common.exception;

/**
 * Created by sangwon on 20. 12. 22..
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
