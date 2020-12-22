package com.kakaopay.assignment.controller;

/**
 * Created by sangwon on 20. 12. 22..
 */

import com.kakaopay.assignment.dto.DistributionRequest;
import com.kakaopay.assignment.dto.TokenResponse;
import com.kakaopay.assignment.service.DistributionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/distribution")
public class DistributionController {
    final DistributionService distributionService;

    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    @PostMapping
    public ResponseEntity<String> distribute(@RequestHeader(value = "X-USER-ID") long userId,
                                             @RequestHeader(value = "X-ROOM-ID") long roomId,
                                             @Valid @RequestBody DistributionRequest request) {
        return ResponseEntity
                .ok(distributionService.distribute(userId, roomId, request.getAmount(), request.getPeopleNumber()));
    }


    @PutMapping
    public ResponseEntity<Long> receive(@RequestHeader(value = "X-USER-ID") long userId,
                                        @RequestHeader(value = "X-ROOM-ID") long roomId,
                                        @RequestParam("tokenKey") String tokenKey) {
        ;
        return ResponseEntity
                .ok(distributionService.receive(userId, roomId, tokenKey));
    }


    @GetMapping
    public ResponseEntity<TokenResponse> check(@RequestHeader(value = "X-USER-ID") long userId,
                                               @RequestHeader(value = "X-ROOM-ID") long roomId,
                                               @RequestParam("tokenKey") String tokenKey) {
        return ResponseEntity.ok(
                TokenResponse.of(distributionService.checkToken(userId, roomId, tokenKey))
        );
    }

}