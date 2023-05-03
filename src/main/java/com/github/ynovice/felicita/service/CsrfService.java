package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.response.CsrfResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface CsrfService {

    CsrfResponse getToken(HttpServletRequest request);
}
