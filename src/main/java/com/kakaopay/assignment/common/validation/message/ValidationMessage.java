package com.kakaopay.assignment.common.validation.message;

import lombok.Getter;

/**
 * Created by sangwon on 20. 12. 23..
 */
public enum ValidationMessage {
    TOKEN_NOT_EXIST("토큰이 존재하지 않습니다."),
    HAS_TO_BE_OWNER("본인것만 조회 가능합니다."),
    TOKEN_SEARCH_PERIOD_EXPIRED("토큰 조회 기간이 만료되었습니다."),
    PEOPLE_OVER_AMOUNT("금액이 인원수 보다 커야 합니다."),
    USER_NOT_EXIST("유저가 존재하지 않습니다."),
    NOT_ENOUGH_BALANCE("잔액이 부족합니다."),
    TOKEN_VALID_PERIOD_EXPIRED("토큰 유효기간이 만료되었습니다."),
    OWNER_CANNOT_RECEIVE("본인이 공유한 항목은 받을 수 없습니다."),
    ALREADY_RECEIVED("이미 받은 항목입니다."),
    NO_ITEMS_LEFT("모두 받은 항목입니다.");
    @Getter
    String message;

    ValidationMessage(String message) {
        this.message = message;
    }
}
