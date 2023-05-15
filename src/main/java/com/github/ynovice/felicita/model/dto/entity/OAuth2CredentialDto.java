package com.github.ynovice.felicita.model.dto.entity;

import com.github.ynovice.felicita.model.entity.OAuth2Credential;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class OAuth2CredentialDto {

    private static final String DATE_TIME_PRESENTATION_FORMAT = "dd.MM.YYYY в HH:mm";

    private String externalId;
    private String authServer;
    private String presentation;
    private String createdAt;

    public static OAuth2CredentialDto fromEntity(OAuth2Credential oAuth2Credential) {

        OAuth2CredentialDto dto = new OAuth2CredentialDto();

        dto.setExternalId(oAuth2Credential.getExternalId());
        dto.setAuthServer(oAuth2Credential.getAuthServer().name());
        dto.setPresentation(oAuth2Credential.getPresentation());
        dto.setCreatedAt(
                oAuth2Credential.getCreatedAt().format(DateTimeFormatter.ofPattern(DATE_TIME_PRESENTATION_FORMAT))
        );

        return dto;
    }
}
