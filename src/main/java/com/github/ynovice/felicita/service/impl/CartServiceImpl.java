package com.github.ynovice.felicita.service.impl;

import com.github.ynovice.felicita.model.entity.Cart;
import com.github.ynovice.felicita.model.entity.User;
import com.github.ynovice.felicita.repository.CartRepository;
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

    @Override
    public Cart getByPrincipal(@NonNull OAuth2User principal) {

        User user = userService.getUser(principal);

        return user.getCart() != null ?
                user.getCart() :
                buildNewCartForUser(user);
    }

    @Override
    public void saveAndFlush(@NonNull Cart cart) {
        cartRepository.saveAndFlush(cart);
    }

    private Cart buildNewCartForUser(@NonNull User user) {

        Cart cart = new Cart();
        cart.setEntries(new ArrayList<>());
        cart.setTotalPrice(0);
        cart.setTotalItems(0);
        cart.setUser(user);

        user.setCart(cart);

        return cart;
    }
}
