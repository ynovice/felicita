import "../css/ItemPage.css";
import withHeader from "../hoc/withHeader";
import inCage from "../hoc/inCage";
import React, {useContext, useEffect, useState} from "react";
import Api from "../Api";
import {useParams} from "react-router-dom";
import NotFoundException from "../exception/NotFoundException";
import ErrorPage from "./ErrorPage";
import CartWidget from "../components/CartWidget";
import {AppContext, ServerState} from "../contexts/AppContext";
import RequestAbortedException from "../exception/RequestAbortedException";

function ItemPage() {

    const appContext = useContext(AppContext);

    const { id: itemId } = useParams();

    const ItemState = {
        LOADING: "LOADING",
        PRESENT: "PRESENT",
        EMPTY: "EMPTY"
    }

    const [item, setItem] = useState(null);
    const [itemState, setItemState] = useState(ItemState.LOADING);

    useEffect(() => {

        const abortController = new AbortController();

        if(appContext.serverState !== ServerState.AVAILABLE) return;

        Api.getItemById(itemId, abortController.signal)
            .then(retrievedItem => {
                setItem(retrievedItem);
                setItemState(ItemState.PRESENT);
            }).catch(e => {
                if(e instanceof RequestAbortedException) return null;
                if(e instanceof NotFoundException) {
                    setItem(null);
                    setItemState(ItemState.EMPTY);
                }
            });

        return () => abortController.abort();
    }, [ItemState.EMPTY, ItemState.PRESENT, appContext.serverState, itemId])

    const [displayedImageIndex, setDisplayedImageIndex] = useState(0);
    const getImageIdByIndex = i => item["images"][i]["id"];

    const [chosenSizeIndex, setChosenSizeIndex] = useState(0);
    const getSizeByIndex = i => item["sizesQuantities"][i]["size"];
    const getQuantityBySizeIndex = i => item["sizesQuantities"][i]["quantity"];


    if(appContext.serverState === ServerState.UNDEFINED) {
        return null;
    }

    if(appContext.serverState === ServerState.UNAVAILABLE) {
        return <ErrorPage errorMessage="Не можем показать подробности о товаре - сервер недоступен."/>
    }

    if(itemState === ItemState.LOADING) {
        return null;
    }

    if(itemState === ItemState.EMPTY) {
        return <ErrorPage errorMessage={"Товар с id " + itemId + " не найден."}/>;
    }

    return (
        <div className="ItemPage">

            <div className="item-images">
                <div className="image-container">
                    {item["images"].length > 0 ?
                        <img src={Api.getImageUrlByImageId(getImageIdByIndex(displayedImageIndex))}
                             alt="Фотография товара"/> :
                        <img src="/ui/item-placeholder.png" alt="У товара нет фотографий"/>
                    }

                </div>
                {item["images"].length > 1 &&
                    <div className="image-selectors">
                        {item["images"].map((image, i) => {

                            if(i === displayedImageIndex)
                                return <span key={image["id"]}>{i + 1}</span>;

                            return <span key={image["id"]}
                                      className="link"
                                      onClick={() => setDisplayedImageIndex(i)}>{i + 1}</span>;
                        })}
                    </div>
                }
            </div>

            <div className="item-info">
                <div className="name-description-section">
                    <h1 className="item-name">{item["name"]}</h1>
                    {item["description"].length > 0 && <p className="item-description">{item["description"]}</p>}
                </div>

                <div className="item-properties">
                    <div className="regular-properties">
                        {item["categories"].length > 0 &&
                            <div className="property">
                                <div className="name">Категории</div>
                                <div className="values">
                                    {item["categories"].map((category, index) => {
                                        return (
                                            <React.Fragment key={category["id"]}>
                                                <a href="#" className="link">{category["name"]}</a>
                                                {index !== item["categories"].length - 1 && ", "}
                                            </React.Fragment>
                                        );
                                    })}
                                </div>
                            </div>
                        }
                        {item["materials"].length > 0 &&
                            <div className="property">
                                <div className="name">Материалы</div>
                                <div className="values">
                                    {item["materials"].map((material, index) => {
                                        return (
                                            <React.Fragment key={material["id"]}>
                                                <a href="#" className="link">{material["name"]}</a>
                                                {index !== item["materials"].length - 1 && ", "}
                                            </React.Fragment>
                                        );
                                    })}
                                </div>
                            </div>
                        }
                        {item["colors"].length > 0 &&
                            <div className="property">
                                <div className="name">Цвета</div>
                                <div className="values">
                                    {item["colors"].map((color, index) => {
                                        return (
                                            <React.Fragment key={color["id"]}>
                                                <a href="#" className="link">{color["name"]}</a>
                                                {index !== item["colors"].length - 1 && ", "}
                                            </React.Fragment>
                                        );
                                    })}
                                </div>
                            </div>
                        }
                        <div className="property">
                            <div className="name">Принт</div>
                            <div className="values">
                                <a href="#" className="link">{item["hasPrint"] ? "Да" : "Нет"}</a>
                            </div>
                        </div>
                    </div>
                    <div>
                        <div className="property">
                            <div className="name">Цена</div>
                            <div className="price-value">{item["price"] + "₽"}</div>
                        </div>

                        {item["sizesQuantities"].length > 0 &&
                            <div className="property">
                                <div className="name">Размер</div>
                                <div className="size-values">
                                    <div className="chosen-size">{getSizeByIndex(chosenSizeIndex)["name"]}</div>
                                    {item["sizesQuantities"].length > 1 &&
                                        <div className="other-sizes">
                                            <span>Другие:</span>
                                            {item["sizesQuantities"].map((sq, i) => {
                                                return i !== chosenSizeIndex &&
                                                    <span key={sq["size"]["id"]}
                                                          className="link"
                                                          onClick={() => setChosenSizeIndex(i)}>
                                                    {sq["size"]["name"]}
                                                </span>
                                            })}
                                        </div>
                                    }
                                </div>
                            </div>
                        }
                    </div>
                </div>

                {item["sizesQuantities"].length > 0 ?
                    <CartWidget itemId={itemId}
                                chosenSizeId={getSizeByIndex(chosenSizeIndex)["id"]}
                                maxQuantity={getQuantityBySizeIndex(chosenSizeIndex)}/> :
                    <CartWidget itemId={item} chosenSizeId={null} maxQuantity={0}/>
                }
            </div>

        </div>
    )
}

export default inCage(withHeader(ItemPage));