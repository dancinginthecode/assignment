package com.kakaopay.assignment.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by sangwon on 20. 12. 22..
 */
/*
 * ○ 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은
 * 사용자 아이디] 리스트)
 */
public class TokenDto {
    LocalDateTime distributeDate;
    long amount;
    long completedAmount;
    List<Long> sharedAmountList;
    List<Long> sharedUsers;
}
