package com.github.ynovice.felicita.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class OAuth2CredentialId implements Serializable {

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private String externalId;

    @Enumerated(EnumType.STRING)
    private AuthServer authServer;
}
