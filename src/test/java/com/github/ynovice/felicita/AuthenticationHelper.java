package com.github.ynovice.felicita;

import com.github.ynovice.felicita.model.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthenticationHelper {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2AuthorizedClient createAuthorizedClient(OAuth2AuthenticationToken authenticationToken) {

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "a",
                Instant.now(),
                Instant.now().plus(Duration.ofDays(1))
        );

        ClientRegistration clientRegistration = clientRegistrationRepository
                .findByRegistrationId(authenticationToken.getAuthorizedClientRegistrationId());

        return new OAuth2AuthorizedClient(clientRegistration, authenticationToken.getName(), accessToken);
    }

    public OAuth2AuthenticationToken createToken() {

        OAuth2User oAuth2User = createOAuth2User(
                "1234567890",
                "Defautl Test User",
                "samndijuhuiad938@gmail.com",
                Role.USER
        );
        return new OAuth2AuthenticationToken(
                oAuth2User,
                oAuth2User.getAuthorities(),
                "login-client"
        );
    }

    public OAuth2User createOAuth2User(String externalId, String username, String email, Role role) {

        Set<GrantedAuthority> authorities = new HashSet<>(
                Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + role)
                )
        );

        Map<String, Object> attributes = Map.of(
                "sub", externalId,
                "name", username,
                "email", email,
                "iss", "https://google.com"
        );

        return new DefaultOAuth2User(authorities, attributes, "sub");
    }
}
