package com.github.ynovice.felicita.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CsrfResponse {

    private String csrfHeaderName;
    private String csrfToken;
}
