package com.kakaopay.assignment.controller;

/**
 * Created by sangwon on 20. 12. 22..
 */

import com.kakaopay.assignment.common.ApiResponse;
import com.kakaopay.assignment.common.validation.AmountGreaterThanPeopleNumber;
import com.kakaopay.assignment.dto.DistributionRequest;
import com.kakaopay.assignment.dto.TokenResponse;
import com.kakaopay.assignment.service.DistributionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@RestController
@Validated
@RequestMapping("/distribution")
public class DistributionController {
    final DistributionService distributionService;

    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> distribute(@RequestHeader(value = "X-USER-ID") @NotNull Long userId,
                                                  @RequestHeader(value = "X-ROOM-ID") @NotNull Long roomId,
                                                  @RequestBody @Valid @AmountGreaterThanPeopleNumber DistributionRequest request) {
        return ResponseEntity
                .ok(ApiResponse
                        .of(distributionService.distribute(userId, roomId, request.getAmount(), request.getPeopleNumber()))
                );
    }


    @PutMapping
    public ResponseEntity<ApiResponse> receive(@RequestHeader(value = "X-USER-ID") @NotNull Long userId,
                                               @RequestHeader(value = "X-ROOM-ID") @NotNull Long roomId,
                                               @RequestParam("tokenKey") @Pattern(regexp = "^[A-Za-z]{3}$") String tokenKey) {
        return ResponseEntity
                .ok(ApiResponse
                        .of(distributionService.receive(userId, roomId, tokenKey))
                );
    }


    @GetMapping
    public ResponseEntity<ApiResponse> check(@RequestHeader(value = "X-USER-ID") @NotNull Long userId,
                                             @RequestHeader(value = "X-ROOM-ID") @NotNull Long roomId,
                                             @RequestParam("tokenKey") @Pattern(regexp = "^[A-Za-z]{3}$") String tokenKey) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        TokenResponse.of(distributionService.checkToken(userId, roomId, tokenKey))
                )
        );
    }

}