package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.model.response.CsrfResponse;
import com.github.ynovice.felicita.service.CsrfService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

@Service
public class CsrfServiceImpl implements CsrfService {

    @Override
    public CsrfResponse getToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        return new CsrfResponse(csrfToken.getHeaderName(), csrfToken.getToken());
    }
}
