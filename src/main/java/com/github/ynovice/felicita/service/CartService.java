package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.Cart;
import lombok.NonNull;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface CartService {

    Cart getByPrincipal(@NonNull OAuth2User principal);

    Cart appendOneItemBySize(Long itemId, Long sizeId, @NonNull OAuth2User oAuth2User);

    Cart removeItemsBySize(Long itemId, Long sizeId, @NonNull OAuth2User oAuth2User);

    Cart removeItemsBySize(Long itemId, Long sizeId, @NonNull OAuth2User oAuth2User, Integer amount);
}
