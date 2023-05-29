package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.ReserveDto;
import com.github.ynovice.felicita.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserve")
@RequiredArgsConstructor
public class ReserveController {

    private final ReserveService reserveService;

    @Secured("ROLE_USER")
    @PostMapping
    public ResponseEntity<ReserveDto> reserveAllItemsInCart(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(
                ReserveDto.fromEntity(reserveService.reserveAllItemsInCart(principal))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReserveDto> getById(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(
                ReserveDto.fromEntity(reserveService.getById(id, principal))
        );
    }

    @GetMapping
    public ResponseEntity<List<ReserveDto>> getAllByUser(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(
            reserveService.getAllByUser(principal)
                    .stream()
                    .map(ReserveDto::fromEntity)
                    .toList()
        );
    }
}
