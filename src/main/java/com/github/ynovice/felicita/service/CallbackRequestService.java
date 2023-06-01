package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.dto.request.CreateCallbackRequestDto;
import com.github.ynovice.felicita.model.entity.CallbackRequest;
import lombok.NonNull;

import java.util.List;

public interface CallbackRequestService {

    CallbackRequest create(String name, String phone);

    List<CallbackRequest> getAll();

    void deleteById(Long id);
}
