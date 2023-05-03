package com.github.ynovice.felicita.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "oauth2_credentials")
@Getter
@Setter
public class OAuth2Credential {

    @EmbeddedId
    private OAuth2CredentialId id;

    @Column(nullable = false)
    private String presentation;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    public String getExternalId() {
        return id.getExternalId();
    }

    public AuthServer getAuthServer() {
        return id.getAuthServer();
    }
}
