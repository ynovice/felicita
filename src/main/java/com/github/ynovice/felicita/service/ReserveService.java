package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.Reserve;
import lombok.NonNull;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReserveService {

    Reserve reserveAllItemsInCart(@NonNull OAuth2User oAuth2User);

    Reserve getById(Long id, @NonNull OAuth2User oAuth2User);

    List<Reserve> getAllByUser(@NonNull OAuth2User oAuth2User);
}
