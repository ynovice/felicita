package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.CartDto;
import com.github.ynovice.felicita.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Secured("ROLE_USER")
    @GetMapping
    public ResponseEntity<CartDto> getCartByPrincipal(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(
                CartDto.fromEntity(cartService.getByPrincipal(principal))
        );
    }
}
