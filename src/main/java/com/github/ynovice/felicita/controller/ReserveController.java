package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.ReserveDto;
import com.github.ynovice.felicita.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reserve")
@RequiredArgsConstructor
public class ReserveController {

    private final ReserveService reserveService;

    @PostMapping
    public ResponseEntity<ReserveDto> reserveAllItemsInCart(@AuthenticationPrincipal OAuth2User principal) {

        return ResponseEntity.ok(
                ReserveDto.fromEntity(reserveService.reserveAllItemsInCart(principal))
        );
    }
}
