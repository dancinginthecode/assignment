package com.kakaopay.assignment.common.exception;

/**
 * Created by sangwon on 20. 12. 22..
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
