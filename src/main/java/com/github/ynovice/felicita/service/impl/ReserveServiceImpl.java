package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.model.entity.*;
import com.github.ynovice.felicita.repository.CartEntryRepository;
import com.github.ynovice.felicita.repository.CartRepository;
import com.github.ynovice.felicita.repository.ReserveRepository;
import com.github.ynovice.felicita.repository.SizeQuantityRepository;
import com.github.ynovice.felicita.service.CartService;
import com.github.ynovice.felicita.service.ItemService;
import com.github.ynovice.felicita.service.ReserveService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService {

    private final ReserveRepository  reserveRepository;

    private final CartService cartService;
    private final ItemService itemService;

    private final CartRepository cartRepository;
    private final CartEntryRepository cartEntryRepository;
    private final SizeQuantityRepository sizeQuantityRepository;

    @Override
    @Transactional
    public Reserve reserveAllItemsInCart(@NonNull OAuth2User oAuth2User) {

        Cart cart = cartService.getByPrincipal(oAuth2User);

        Reserve reserve = createAndLinkReserve(cart);

        cart.setTotalItems(0);
        cart.setTotalPrice(0);

        for(CartEntry cartEntry : cart.getEntries()) {

            createAndLinkReserveEntry(cartEntry, reserve);

            cartEntry.getSizesQuantities().clear();
            sizeQuantityRepository.deleteAllByCartEntry(cartEntry);
        }

        cart.getEntries().clear();
        cartEntryRepository.deleteAllByCart(cart);

        cartRepository.saveAndFlush(cart);
        reserveRepository.saveAndFlush(reserve);

        itemService.updateItemQuantitiesAfterReserve(reserve);

        return reserve;
    }

    private Reserve createAndLinkReserve(@NonNull Cart cart) {

        User user = cart.getUser();

        Reserve reserve = new Reserve();
        reserve.setEntries(new ArrayList<>());
        reserve.setTotalPrice(cart.getTotalPrice());
        reserve.setTotalItems(cart.getTotalItems());
        reserve.setCreatedAt(ZonedDateTime.now());
        reserve.setUser(user);

        user.getReserves().add(reserve);

        return reserve;
    }

    private void createAndLinkReserveEntry(@NonNull CartEntry cartEntry, @NonNull Reserve reserve) {

        ReserveEntry reserveEntry = new ReserveEntry();
        reserveEntry.setSizesQuantities(new ArrayList<>());
        reserveEntry.setItem(cartEntry.getItem());
        reserveEntry.setPricePerItem(cartEntry.getItem().getPrice());

        for(SizeQuantity cartEntrySQ : cartEntry.getSizesQuantities()) {

            SizeQuantity reserveEntrySQ = new SizeQuantity();
            reserveEntrySQ.setQuantity(cartEntrySQ.getQuantity());
            reserveEntrySQ.setSize(cartEntrySQ.getSize());

            reserveEntrySQ.setReserveEntry(reserveEntry);
            reserveEntry.getSizesQuantities().add(reserveEntrySQ);
        }

        reserveEntry.setReserve(reserve);
        reserve.getEntries().add(reserveEntry);
    }
}
