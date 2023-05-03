package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService {

    User registerIfNotRegistered(OAuth2User oAuth2User);

    User getUser(OAuth2User oAuth2User);
}
