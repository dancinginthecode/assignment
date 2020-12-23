package com.kakaopay.assignment.controller;

/**
 * Created by sangwon on 20. 12. 22..
 */

import com.kakaopay.assignment.common.ApiResponse;
import com.kakaopay.assignment.dto.DistributionDto;
import com.kakaopay.assignment.dto.DistributionRequest;
import com.kakaopay.assignment.dto.TokenDto;
import com.kakaopay.assignment.dto.TokenResponse;
import com.kakaopay.assignment.service.DistributionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/distribution")
public class DistributionController {
    final DistributionService distributionService;

    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> distribute(@RequestHeader(value = "X-USER-ID") Long userId,
                                                  @RequestHeader(value = "X-ROOM-ID") Long roomId,
                                                  @RequestBody DistributionRequest request) {
        return ResponseEntity
                .ok(ApiResponse
                        .of(distributionService.distribute(
                                DistributionDto.builder()
                                        .userId(userId)
                                        .roomId(roomId)
                                        .amount(request.getAmount())
                                        .peopleNumber(request.getPeopleNumber())
                                        .build()
                                )
                        )
                );
    }


    @PutMapping
    public ResponseEntity<ApiResponse> receive(@RequestHeader(value = "X-USER-ID") Long userId,
                                               @RequestHeader(value = "X-ROOM-ID") Long roomId,
                                               @RequestParam("tokenKey") String tokenKey) {
        return ResponseEntity
                .ok(ApiResponse
                        .of(distributionService.receive(
                                TokenDto.builder()
                                        .userId(userId)
                                        .roomId(roomId)
                                        .tokenKey(tokenKey)
                                        .build()
                                )
                        )
                );
    }


    @GetMapping
    public ResponseEntity<ApiResponse> check(@RequestHeader(value = "X-USER-ID") Long userId,
                                             @RequestHeader(value = "X-ROOM-ID") Long roomId,
                                             @RequestParam("tokenKey") String tokenKey) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        TokenResponse.of(distributionService.checkToken(
                                TokenDto.builder()
                                        .userId(userId)
                                        .roomId(roomId)
                                        .tokenKey(tokenKey)
                                        .build()
                                )
                        )
                )
        );
    }

}