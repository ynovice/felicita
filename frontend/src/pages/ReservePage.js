import withHeaderAndFooter from "../hoc/withHeaderAndFooter";
import inCage from "../hoc/inCage";
import requiresUser from "../hoc/requiresUser";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import Api from "../Api";
import NotFoundException from "../exception/NotFoundException";
import NotAuthorizedException from "../exception/NotAuthorizedException";
import ErrorPage from "./ErrorPage";
import "../css/ReservePage.css";
import FailedRequestException from "../exception/FailedRequestException";

function ReservePage() {

    const ReserveState = {
        LOADING: "LOADING",
        PRESENT: "PRESENT",
        NOT_FOUND: "NOT_FOUND",
        NOT_AUTHORIZED: "NOT_AUTHORIZED",
        ERROR: "ERROR"
    }

    const { id } = useParams();

    const [reserve, setReserve] = useState(null);
    const [reserveState, setReserveState] = useState(ReserveState.LOADING);

    useEffect(() => {

        const abortController = new AbortController();

        Api.getReserveById(id, abortController.signal)
            .then(retrievedReserve => {
                setReserve(retrievedReserve);
                setReserveState(ReserveState.PRESENT);
            })
            .catch(e => {

                if(e instanceof NotFoundException) setReserveState(ReserveState.NOT_FOUND);
                else if (e instanceof NotAuthorizedException) setReserveState(ReserveState.NOT_AUTHORIZED);
                else if (!(e instanceof FailedRequestException)) setReserveState(ReserveState.ERROR);
            });

        return () => abortController.abort();
    }, [ReserveState.ERROR, ReserveState.NOT_AUTHORIZED, ReserveState.NOT_FOUND, ReserveState.PRESENT, id]);

    if(reserveState === ReserveState.LOADING) return;

    if(reserveState === ReserveState.NOT_FOUND)
        return <ErrorPage errorMessage={"Резерв с номером " + id + " не найден."}/>

    if(reserveState === ReserveState.NOT_AUTHORIZED)
        return <ErrorPage errorMessage={"У вас нет доступа к резерву с номером " + id + "."}/>

    if(reserveState === ReserveState.ERROR)
        return <ErrorPage errorMessage={"Произошла неизвестная ошибка при попытке получить данные резерва."}/>

    return (
        <div className="ReservePage">

            <div className="left-side">
                <div className="page-title">Вы зарезервировали эти товары</div>

                <div className="cart-items">
                    {reserve["entries"].map(reserveEntry => {

                        const item = reserveEntry["item"];

                        const imageUrl = item["images"].length > 0 ?
                            Api.getImageUrlByImageId(item["images"][0]["id"]) :
                            "/ui/item-placeholder.png";

                        return reserveEntry["sizesQuantities"].map(sq => {

                            return (
                                <div key={"cart-sq-" + sq["size"]["id"] + "-" + item["id"]} className="cart-item">
                                    <div className="image-container">
                                        <img src={imageUrl} alt={item["name"]}/>
                                    </div>

                                    <div className="cart-item-info">
                                        <div className="cart-item-name">{item["name"]}</div>
                                        <div className="cart-item-size">{sq["size"]["name"]}</div>
                                        <div className="cart-item-total-price">
                                            {item["price"] * sq["quantity"]}₽
                                        </div>

                                        <div className="cart-item-controls noselect disabled">
                                            <div className="left"></div>
                                            <div className="right">
                                                <div className="counter-controls">
                                                    <span className="noselect disabled">{sq["quantity"]}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            );

                        });
                    })}
                </div>

                <div className="cart-summary">
                    <div className="items-total-count">Всего товаров: {reserve["totalItems"]}</div>
                    <div className="items-total-price">
                        <p>Итого</p>
                        <p>{reserve["totalPrice"]}₽</p>
                    </div>
                </div>

                <p className="disclaimer">
                    Для получения товаров вы можете назвать номер резерва сотруднику магазина. Оплата товаров
                    производится во время получения. У вас есть возможность проверить товары в примерочной.
                    Администратор магазина в праве в одностороннем порядке отменить резерв в случае, если вы не
                    будете приходить за товарами долгое время, или по другим обстоятельствам.
                </p>
            </div>

            <div className="right-side">
                <div className="reserve-info">
                    <p>Номер резерва:</p>
                    <p>{reserve["id"]}</p>
                </div>
                <div className="reserve-info">
                    <p>Дата создания:</p>
                    <p>{reserve["createdAtPresentation"]}</p>
                </div>
                <a href="/reserve" className="link">Все резервы</a>
                <a href="/contacts" className="link">Связаться с нами</a>
            </div>

        </div>
    );
}

export default inCage(withHeaderAndFooter(requiresUser(
    ReservePage,
    "Войдите в аккаунт, чтобы просмотреть зарезервированные вами товары"
)));