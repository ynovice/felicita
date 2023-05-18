package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.exception.NotFoundException;
import com.github.ynovice.felicita.model.dto.entity.CartEntryDto;
import com.github.ynovice.felicita.model.entity.CartEntry;
import com.github.ynovice.felicita.service.CartEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ce")
@RequiredArgsConstructor
public class CartEntryController {

    private final CartEntryService cartEntryService;

    @Secured("ROLE_USER")
    @GetMapping
    public ResponseEntity<CartEntryDto> getByUserIdAndItemId(@RequestParam Long itemId,
                                                             @AuthenticationPrincipal OAuth2User principal) {

        CartEntry cartEntry = cartEntryService.getByItemId(itemId, principal)
                .orElseThrow(NotFoundException::new);

        return ResponseEntity.ok(CartEntryDto.fromEntity(cartEntry));
    }

    @Secured("ROLE_USER")
    @PostMapping
    public ResponseEntity<CartEntryDto> incrementItemQuantityInCart(@RequestParam Long itemId,
                                                                    @RequestParam Long sizeId,
                                                                    @AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(
                CartEntryDto.fromEntity(cartEntryService.incrementItemQuantityInCart(itemId, sizeId, principal))
        );
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    public ResponseEntity<CartEntryDto> decrementItemQuantityInCart(@RequestParam Long itemId,
                                                                    @RequestParam Long sizeId,
                                                                    @AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(
                CartEntryDto.fromEntity(cartEntryService.decrementItemQuantityInCart(itemId, sizeId, principal))
        );
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/sq")
    public ResponseEntity<CartEntryDto> removeSizeQuantityFromCartEntry(@RequestParam Long itemId,
                                                                        @RequestParam Long sizeId,
                                                                        @AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(
                CartEntryDto.fromEntity(cartEntryService.removeSizeQuantityFromCartEntry(itemId, sizeId, principal))
        );
    }
}
