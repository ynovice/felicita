package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.UserDto;
import com.github.ynovice.felicita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> currentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return ResponseEntity.ok(
                UserDto.fromEntity(userService.getUser(oAuth2User))
        );
    }
}
