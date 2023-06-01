package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.ReserveDto;
import com.github.ynovice.felicita.model.dto.response.ReservesPageDto;
import com.github.ynovice.felicita.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ReservesPageDto> getAllByUser(@RequestParam(defaultValue = "0") int page,
                                                        @AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(
                ReservesPageDto.fromEntity(reserveService.getAllByUser(page, principal))
        );
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(params = "scope=admin")
    public ResponseEntity<ReservesPageDto> getAll(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(
            ReservesPageDto.fromEntity(reserveService.getAll(page))
        );
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        reserveService.deleteById(id);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/{id}", params = "action=cancel")
    public void cancelById(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        reserveService.cancelById(id, principal);
    }
}
