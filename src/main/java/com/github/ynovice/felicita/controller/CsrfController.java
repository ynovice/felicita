package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.response.CsrfResponse;
import com.github.ynovice.felicita.service.CsrfService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/csrf")
@RequiredArgsConstructor
public class CsrfController {

    private final CsrfService csrfService;

    @GetMapping
    public ResponseEntity<CsrfResponse> getCsrfToken(HttpServletRequest request) {
        return ResponseEntity.ok(csrfService.getToken(request));
    }
}
