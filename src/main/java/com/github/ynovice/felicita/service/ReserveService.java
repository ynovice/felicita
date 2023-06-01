package com.github.ynovice.felicita.service;

import com.github.ynovice.felicita.model.entity.Reserve;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public interface ReserveService {

    Reserve reserveAllItemsInCart(@NonNull OAuth2User oAuth2User);

    void cancelById(Long id, @NonNull OAuth2User oAuth2User);

    Reserve getById(Long id, @NonNull OAuth2User oAuth2User);

    Page<Reserve> getAllByUser(int page, @NonNull OAuth2User oAuth2User);

    Page<Reserve> getAll(int page);

    void deleteById(Long id);

//    void removeItemFromReserves(Item item);
}
