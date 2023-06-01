package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.model.dto.request.CreateCallbackRequestDto;
import com.github.ynovice.felicita.model.entity.CallbackRequest;
import com.github.ynovice.felicita.repository.CallbackRequestRepository;
import com.github.ynovice.felicita.service.CallbackRequestService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackRequestServiceImpl implements CallbackRequestService {

    private final CallbackRequestRepository callbackRequestRepository;

    @Override
    public CallbackRequest create(String name, String phone) {

        CallbackRequest callbackRequest = new CallbackRequest();

        callbackRequest.setName(name);
        callbackRequest.setPhone(phone);
        callbackRequest.setCreatedAt(ZonedDateTime.now());

        callbackRequestRepository.saveAndFlush(callbackRequest);

        return callbackRequest;
    }

    @Override
    public List<CallbackRequest> getAll() {
        return callbackRequestRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        callbackRequestRepository.deleteById(id);
    }
}
