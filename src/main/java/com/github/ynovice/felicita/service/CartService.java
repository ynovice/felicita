package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.Cart;
import lombok.NonNull;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface CartService {

    Cart getByPrincipal(OAuth2User principal);

    void saveAndFlush(@NonNull Cart cart);
}
