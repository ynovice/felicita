package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.exception.BadRequestException;
import com.github.ynovice.felicita.model.entity.*;
import com.github.ynovice.felicita.repository.CartEntryRepository;
import com.github.ynovice.felicita.repository.SizeQuantityRepository;
import com.github.ynovice.felicita.service.CartEntryService;
import com.github.ynovice.felicita.service.ItemService;
import com.github.ynovice.felicita.service.SizeService;
import com.github.ynovice.felicita.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartEntryServiceImpl implements CartEntryService {

    private final UserService userService;
    private final ItemService itemService;
    private final SizeService sizeService;

    private final CartEntryRepository cartEntryRepository;

    private final SizeQuantityRepository sizeQuantityRepository;

    @Override
    public Optional<CartEntry> getByItemId(Long itemId, @NonNull OAuth2User principal) {

        User user = userService.getUser(principal);

        return cartEntryRepository.findByItemIdAndUser(itemId, user);
    }

    @Override
    public CartEntry incrementItemQuantityInCart(Long itemId,
                                                 Long sizeId,
                                                 @NonNull OAuth2User principal) {

        User user = userService.getUser(principal);

        Item item = itemService.getById(itemId)
                .orElseThrow(() -> new BadRequestException("Товар с id " + itemId + " не существует"));

        CartEntry cartEntry = cartEntryRepository
                .findByItemIdAndUser(itemId, user)
                .orElseGet(() -> buildCartEntry(user, item));

        int incrementedQuantity = cartEntry.getQuantityBySizeId(sizeId) + 1;
        Integer quantityAvailable = item.getQuantityBySizeId(sizeId);

        if(incrementedQuantity > quantityAvailable)
            throw new BadRequestException(
                "В наличии только " + quantityAvailable + " единиц этого товара"
            );

        Size size = sizeService.getById(sizeId)
                        .orElseThrow(() -> new BadRequestException("Выбран несуществующий размер товара"));

        cartEntry.incrementQuantityBySize(size);

        cartEntryRepository.saveAndFlush(cartEntry);

        return cartEntry;
    }

    @Override
    public CartEntry decrementItemQuantityInCart(Long itemId, Long sizeId, @NonNull OAuth2User principal) {

        User user = userService.getUser(principal);

        CartEntry cartEntry = cartEntryRepository.findByItemIdAndUser(itemId, user)
                .orElseThrow(() -> new BadRequestException("Вы не добавляли этот товар в корзину"));

        SizeQuantity sizeQuantity = cartEntry.getSizeQuantityBySizeId(sizeId)
                .orElseThrow(() -> new BadRequestException("Вы не добавляли этот товар в корзину"));

        sizeQuantity.decrementQuantity();

        if(sizeQuantity.getQuantity() == 0)
            deleteSizeQuantityFromCartEntryWithRepository(sizeQuantity, cartEntry);
        else
            cartEntryRepository.save(cartEntry);

        return cartEntry;
    }

    @Override
    public CartEntry removeSizeQuantityFromCartEntry(Long itemId, Long sizeId, @NonNull OAuth2User principal) {

        User user = userService.getUser(principal);

        CartEntry cartEntry = cartEntryRepository.findByItemIdAndUser(itemId, user)
                .orElseThrow(() -> new BadRequestException("Вы не добавляли этот товар в корзину"));

        SizeQuantity sizeQuantity = cartEntry.getSizeQuantityBySizeId(sizeId)
                .orElseThrow(() -> new BadRequestException("Вы не добавляли этот товар в корзину"));

        deleteSizeQuantityFromCartEntryWithRepository(sizeQuantity, cartEntry);

        return cartEntry;
    }

    private void deleteSizeQuantityFromCartEntryWithRepository(@NonNull SizeQuantity sizeQuantity,
                                                               @NonNull CartEntry cartEntry) {

        cartEntry.removeSizeQuantityBySizeId(sizeQuantity.getSizeId());
        sizeQuantityRepository.delete(sizeQuantity);

        if(cartEntry.getSizesQuantities().isEmpty()) cartEntryRepository.delete(cartEntry);
    }

    private CartEntry buildCartEntry(User user, Item item) {
        CartEntry ce = new CartEntry();
        ce.setUser(user);
        ce.setItem(item);
        ce.setSizesQuantities(new ArrayList<>());
        return ce;
    }
}
