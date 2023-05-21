package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.exception.BadRequestException;
import com.github.ynovice.felicita.model.entity.*;
import com.github.ynovice.felicita.repository.CartEntryRepository;
import com.github.ynovice.felicita.repository.SizeQuantityRepository;
import com.github.ynovice.felicita.service.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartEntryServiceImpl implements CartEntryService {

    private final UserService userService;
    private final ItemService itemService;
    private final SizeService sizeService;
    private final CartService cartService;

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

        Item item = itemService.getById(itemId)
                .orElseThrow(() -> new BadRequestException("Товар с id " + itemId + " не существует"));

        Size size = sizeService.getById(sizeId)
                .orElseThrow(() -> new BadRequestException("Выбран несуществующий размер"));

        Cart cart = cartService.getByPrincipal(principal);

        int incrementedQuantity = cart.getQuantityByItemAndSize(item, size) + 1;
        int quantityAvailable = item.getQuantityBySize(size);

        if(incrementedQuantity > quantityAvailable)
            throw new BadRequestException(
                "В наличии только " + quantityAvailable + " единиц этого товара"
            );

        CartEntry cartEntry = cart.incrementItemQuantityBySize(item, size);

        cartService.saveAndFlush(cart);

        return cartEntry;
    }

    @Override
    public CartEntry decrementItemQuantityInCart(Long itemId, Long sizeId, @NonNull OAuth2User principal) {

        Item item = itemService.getById(itemId)
                .orElseThrow(() -> new BadRequestException("Товар с id " + itemId + " не существует"));

        Size size = sizeService.getById(sizeId)
                .orElseThrow(() -> new BadRequestException("Выбран несуществующий размер"));

        Cart cart = cartService.getByPrincipal(principal);

        int currentQuantity = cart.getQuantityByItemAndSize(item, size);
        if(currentQuantity == 0) {
            throw new BadRequestException("Вы не добавляли этот товар в корзину");
        }

        CartEntry cartEntry = cart.decrementItemQuantityBySize(item, size);

        cartEntry
                .getSizeQuantityBySizeId(sizeId)
                .ifPresent(sq -> {
                    if(sq.getQuantity() == 0) {
                        deleteSizeQuantityFromCartEntryWithRepository(sq, cartEntry);
                    }
                });

        cartService.saveAndFlush(cart);

        return cartEntry;
    }

    @Override
    public CartEntry removeSizeQuantityFromCartEntry(Long itemId, Long sizeId, @NonNull OAuth2User principal) {

        Item item = itemService.getById(itemId)
                .orElseThrow(() -> new BadRequestException("Товар с id " + itemId + " не существует"));

        Size size = sizeService.getById(sizeId)
                .orElseThrow(() -> new BadRequestException("Выбран несуществующий размер"));

        Cart cart = cartService.getByPrincipal(principal);

        int currentQuantity = cart.getQuantityByItemAndSize(item, size);

        if(currentQuantity == 0) {
            throw new BadRequestException("Вы не добавляли этот товар в корзину");
        }

        User user = userService.getUser(principal);

        CartEntry cartEntry = cartEntryRepository.findByItemIdAndUser(itemId, user)
                .orElseThrow(() -> new BadRequestException("Вы не добавляли этот товар в корзину"));

        cartEntry
                .getSizeQuantityBySizeId(sizeId)
                .ifPresent(sq -> deleteSizeQuantityFromCartEntryWithRepository(sq, cartEntry));

        cart.setTotalItems(cart.getTotalItems() - currentQuantity);
        cart.setTotalPrice(cart.getTotalPrice() - currentQuantity * item.getPrice());

        cartService.saveAndFlush(cart);

        return cartEntry;
    }

    private void deleteSizeQuantityFromCartEntryWithRepository(@NonNull SizeQuantity sizeQuantity,
                                                               @NonNull CartEntry cartEntry) {

        cartEntry.removeSizeQuantityBySizeId(sizeQuantity.getSizeId());
        sizeQuantityRepository.delete(sizeQuantity);

        if(cartEntry.getSizesQuantities().isEmpty()) {
            cartEntry.getCart().getEntries().remove(cartEntry);
            cartEntryRepository.delete(cartEntry);
        }
    }
}
