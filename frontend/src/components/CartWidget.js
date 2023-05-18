import "../css/CartWidget.css";
import {useContext, useEffect, useState} from "react";
import {UpdatedUserContext, UserPresenceState} from "../contexts/UserContext";
import {AppContext, ServerState} from "../contexts/AppContext";
import Api from "../Api";
import NotFoundException from "../exception/NotFoundException";
import Button from "./Button";

function CartWidget({itemId, chosenSizeId, maxQuantity=0}) {

    const appContext = useContext(AppContext);
    const userContext = useContext(UpdatedUserContext);

    const CartEntryState = {
        LOADING: "LOADING",
        PRESENT: "PRESENT",
        EMPTY: "EMPTY"
    }

    const [cartEntry, setCartEntry] = useState(null);
    const [cartEntryState, setCartEntryState] = useState(CartEntryState.LOADING);

    useEffect(() => {

        if(appContext.serverState !== ServerState.AVAILABLE
            || userContext.userPresenceState !== UserPresenceState.PRESENT) {
            return;
        }

        const abortController = new AbortController();

        const userId = userContext.user["id"];

        Api.getCartEntryByUserIdAndItemId(userId, itemId)
            .then(retrievedCartEntry => {
                setCartEntry(retrievedCartEntry);
                setCartEntryState(CartEntryState.PRESENT);
            })
            .catch((e) => {
                if(e instanceof NotFoundException) {
                    setCartEntry(null);
                    setCartEntryState(CartEntryState.EMPTY);
                }
            })

        return () => abortController.abort();
    }, [appContext, userContext, itemId, CartEntryState.PRESENT, CartEntryState.EMPTY]);

    const handleAddItemToCartClick = () => {

        Api.incrementItemQuantityInCart(itemId, chosenSizeId)
            .then(updatedCartEntry => {
                setCartEntry(updatedCartEntry);
                setCartEntryState(CartEntryState.PRESENT);
            });
    }

    const handleIncrementItemQuantityInCartClick = () => {

        if(getQuantityByChosenSizeId() + 1 > maxQuantity) {
            return;
        }

        Api.incrementItemQuantityInCart(itemId, chosenSizeId)
            .then(updatedCartEntry => {
                setCartEntry(updatedCartEntry);
                setCartEntryState(CartEntryState.PRESENT);
            });
    }

    const handleDecrementItemQuantityInCartClick = () => {

        Api.decrementItemQuantityInCart(itemId, chosenSizeId)
            .then(updatedCartEntry => {
                setCartEntry(updatedCartEntry);
                setCartEntryState(CartEntryState.PRESENT);
            });
    }

    const handleRemoveSizeQuantityFromCartEntryClick = () => {

        Api.removeSizeQuantityFromCartEntry(itemId, chosenSizeId)
            .then(updatedCartEntry => {
                setCartEntry(updatedCartEntry);
                setCartEntryState(CartEntryState.PRESENT);
            });
    }

    const getQuantityByChosenSizeId = () => {

        for (let i = 0; i < cartEntry["sizesQuantities"].length; i++) {
            if(cartEntry["sizesQuantities"][i]["size"]["id"] === chosenSizeId)
                return cartEntry["sizesQuantities"][i]["quantity"];
        }

        return 0;
    }

    if(appContext.serverState === ServerState.UNDEFINED
        || userContext.userPresenceState === UserPresenceState.LOADING) {
        return null;
    }

    if(appContext.serverState === ServerState.UNAVAILABLE) {
        return <div className="CartWidget">На сервере произошла ошибка при загрузке корзины :(</div>;
    }

    if(maxQuantity === 0) {
        return <div className="CartWidget">Этого товара нет в наличии.</div>;
    }

    if(userContext.userPresenceState === UserPresenceState.EMPTY) {
        return <div className="CartWidget">Войдите в аккаунт, чтобы пользоваться корзиной</div>;
    }

    if(cartEntryState === CartEntryState.LOADING) {
        return null;
    }

    if(cartEntryState === CartEntryState.EMPTY || getQuantityByChosenSizeId() === 0) {
        return (
            <div className="CartWidget">
                <span></span>
                <Button value={"В корзину"} onClick={() => handleAddItemToCartClick()}/>
            </div>
        );
    }

    return (
        <div className="CartWidget">
            <div className="title">В корзине</div>
            <div className="controls">
                <div className="inverter">
                    <div className="cart-quantity-counter">
                        <span className="noselect"
                              onClick={() => handleDecrementItemQuantityInCartClick()}>-</span>
                        <span className="noselect disabled">{getQuantityByChosenSizeId()}</span>
                        <span className={"noselect" + (getQuantityByChosenSizeId() === maxQuantity ? " disabled" : "")}
                              onClick={() => handleIncrementItemQuantityInCartClick()}>
                            +
                        </span>
                    </div>
                    {getQuantityByChosenSizeId() === maxQuantity && <p>Больше не добавить</p>}
                </div>
                <span className="link danger" onClick={() => handleRemoveSizeQuantityFromCartEntryClick()}>Убрать</span>
            </div>

        </div>
    );
}

export default CartWidget;