package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.exception.InternalServerError;
import com.github.ynovice.felicita.model.entity.*;
import com.github.ynovice.felicita.repository.UserRepository;
import com.github.ynovice.felicita.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public User registerIfNotRegistered(OAuth2User oAuth2User) {

        String externalId = oAuth2User.getName();
        AuthServer authServer = defineAuthServer(oAuth2User);

        return userRepository
                .findByExternalIdAndAuthServer(externalId, authServer)
                .orElseGet(() -> {

                    OAuth2CredentialId credentialId = new OAuth2CredentialId();
                    credentialId.setExternalId(externalId);
                    credentialId.setAuthServer(authServer);

                    OAuth2Credential credential = new OAuth2Credential();
                    credential.setId(credentialId);
                    credential.setPresentation(oAuth2User.getAttribute("email"));  // only appropriate for Google
                    credential.setCreatedAt(ZonedDateTime.now());

                    User user = new User();
                    credentialId.setUser(user);
                    user.setRole(Role.USER);
                    user.setOAuth2Credentials(Collections.singleton(credential));
                    user.setUsername(oAuth2User.getAttribute("name"));

                    userRepository.saveAndFlush(user);

                    return user;
                });
    }

    @Override
    public User getUser(OAuth2User oAuth2User) {

        String externalId = oAuth2User.getName();
        AuthServer authServer = defineAuthServer(oAuth2User);

        return userRepository
                .findByExternalIdAndAuthServer(externalId, authServer)
                .orElseThrow(
                        () -> new InternalServerError(
                                "Что-то пошло не так, попробуйте войти в аккаунт заново"
                        )
                );
    }

    private AuthServer defineAuthServer(@NonNull OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();

        if(attributes.containsKey("iss") && attributes.get("iss").toString().contains("google.com")) {
            return AuthServer.GOOGLE;
        }

        throw new InternalServerError("Произошла ошибка при попытке распознать сервер авторизации");
    }
}
