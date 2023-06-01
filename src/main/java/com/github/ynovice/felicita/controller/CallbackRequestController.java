package com.github.ynovice.felicita.controller;

import com.github.ynovice.felicita.model.dto.entity.CallbackRequestDto;
import com.github.ynovice.felicita.model.dto.request.CreateCallbackRequestDto;
import com.github.ynovice.felicita.service.CallbackRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/callbackrequest")
@RequiredArgsConstructor
public class CallbackRequestController {

    private final CallbackRequestService callbackRequestService;

    @GetMapping(params = {"name", "phone"})
    public ResponseEntity<CallbackRequestDto> create(@RequestParam String name,
                                                     @RequestParam String phone) {
        return ResponseEntity.ok(
                CallbackRequestDto.fromEntity(callbackRequestService.create(name, phone))
        );
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<CallbackRequestDto>> getAll() {
        return ResponseEntity.ok(
                callbackRequestService.getAll()
                        .stream()
                        .map(CallbackRequestDto::fromEntity)
                        .toList()
        );
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        callbackRequestService.deleteById(id);
    }
}
