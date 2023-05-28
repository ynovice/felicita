package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.exception.BadRequestException;
import com.github.ynovice.felicita.model.entity.*;
import com.github.ynovice.felicita.repository.*;
import com.github.ynovice.felicita.service.CartService;
import com.github.ynovice.felicita.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final UserService userService;

    private final ItemRepository itemRepository;
    private final SizeRepository sizeRepository;
    private final CartEntryRepository cartEntryRepository;
    private final SizeQuantityRepository sizeQuantityRepository;

    @Override
    public Cart getByPrincipal(@NonNull OAuth2User principal) {

        User user = userService.getUser(principal);

        createAndLinkCartIfNotExists(user);
        Cart cart = user.getCart();

        updateCart(cart);

        cartRepository.saveAndFlush(cart);

        return cart;
    }

    @Override
    public Cart appendOneItemBySize(Long itemId, Long sizeId, @NonNull OAuth2User oAuth2User) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new BadRequestException("Выбранный товар не существует"));

        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(() -> new BadRequestException("Выбранный размер не существует"));

        Cart cart = getByPrincipal(oAuth2User);
        CartEntry cartEntry = cart.getCartEntryByItem(item);
        SizeQuantity cartEntrySQ = cartEntry.getSizeQuantityBySize(size);

        SizeQuantity itemSQ = item.getSizeQuantityBySize(size)
                .orElseThrow(() -> new BadRequestException("На складе нет выбранного вами товара"));

        if(cartEntrySQ.getQuantity() + 1 > itemSQ.getQuantity()) {
            throw new BadRequestException("На складе есть только " + itemSQ.getQuantity() + " единиц товара");
        }

        cartEntrySQ.updateQuantity(1);
        cart.updateTotalItems(1);
        cart.updateTotalPrice(item.getPrice());

        cartRepository.saveAndFlush(cart);
        return cart;
    }

    @Override
    public Cart removeItemsBySize(Long itemId, Long sizeId, @NonNull OAuth2User oAuth2User) {
        return removeItemsBySize(itemId, sizeId, oAuth2User, null);
    }

    @Override
    public Cart removeItemsBySize(Long itemId, Long sizeId, @NonNull OAuth2User oAuth2User, Integer amount) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new BadRequestException("Выбранный товар не существует"));

        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(() -> new BadRequestException("Выбранный размер не существует"));

        Cart cart = getByPrincipal(oAuth2User);
        CartEntry cartEntry = cart.getCartEntryByItem(item);
        SizeQuantity cartEntrySQ = cartEntry.getSizeQuantityBySize(size);

        if(amount == null)
            amount = cartEntrySQ.getQuantity();

        if(amount <= 0)
            throw new BadRequestException("Количество удаляемых товаров должно быть больше 0");

        if(cartEntrySQ.getQuantity() < amount)
            throw new BadRequestException("У вас в корзине нет " + amount + " единиц такого товара");

        cartEntrySQ.updateQuantity(-amount);

        if(cartEntrySQ.getQuantity() == 0) {
            cartEntry.getSizesQuantities().remove(cartEntrySQ);
            sizeQuantityRepository.delete(cartEntrySQ);
        }

        if(cartEntry.getSizesQuantities().size() == 0) {
            cart.getEntries().remove(cartEntry);
            cartEntryRepository.delete(cartEntry);
        }

        cart.updateTotalItems(-amount);
        cart.updateTotalPrice(-amount * item.getPrice());

        cartRepository.saveAndFlush(cart);

        return cart;
    }

    private void updateCart(@NonNull Cart cart) {

        for(CartEntry cartEntry : cart.getEntries()) {

            Item item = cartEntry.getItem();

            for(SizeQuantity  cartEntrySQ : cartEntry.getSizesQuantities()) {

                Size size = cartEntrySQ.getSize();

                int amountOfItemsGone = cartEntrySQ.getQuantity() - item.getQuantityBySize(size);

                if(amountOfItemsGone > 0) {

                    CartEntry.SizeQuantityPrevState sqps = new CartEntry.SizeQuantityPrevState(cartEntrySQ);
                    cartEntry.addSizeQuantityPrevState(sqps);

                    cartEntrySQ.setQuantity(cartEntrySQ.getQuantity() - amountOfItemsGone);

                    cart.updateTotalItems(-amountOfItemsGone);
                    cart.updateTotalPrice(-item.getPrice() * amountOfItemsGone);
                }
            }
        }
    }

    private void createAndLinkCartIfNotExists(@NonNull User user) {

        if(user.getCart() == null) {

            Cart cart = new Cart();
            cart.setEntries(new ArrayList<>());
            cart.setTotalPrice(0);
            cart.setTotalItems(0);
            cart.setUser(user);

            user.setCart(cart);
        }
    }
}
