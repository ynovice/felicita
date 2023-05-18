package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.CartEntry;
import lombok.NonNull;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

public interface CartEntryService {

    Optional<CartEntry> getByItemId(Long itemId, @NonNull OAuth2User principal);

    CartEntry incrementItemQuantityInCart(Long itemId, Long sizeId, @NonNull OAuth2User principal);

    CartEntry decrementItemQuantityInCart(Long itemId, Long sizeId, @NonNull OAuth2User principal);

    CartEntry removeSizeQuantityFromCartEntry(Long itemId, Long sizeId, @NonNull OAuth2User principal);
}
