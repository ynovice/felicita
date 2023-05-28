package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.Reserve;
import lombok.NonNull;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public interface ReserveService {

    Reserve reserveAllItemsInCart(@NonNull OAuth2User oAuth2User);
}
