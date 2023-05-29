import withHeaderAndFooter from "../hoc/withHeaderAndFooter";
import requiresUser from "../hoc/requiresUser";
import "../css/ReservesListPage.css";
import {useEffect, useState} from "react";
import ErrorPage from "./ErrorPage";
import Api from "../Api";
import NotAuthorizedException from "../exception/NotAuthorizedException";
import FailedRequestException from "../exception/FailedRequestException";

function ReservesListPage() {

    const ReservesState = {
        LOADED: "LOADED",
        LOADING: "LOADING",
        NOT_AUTHORIZED: "NOT_AUTHORIZED",
        ERROR: "ERROR"
    };

    const [reserves, setReserves] = useState([]);
    const [reservesState, setReservesState] = useState(ReservesState.LOADING);

    useEffect(() => {

        const abortController = new AbortController();

        Api.getReservesForCurrentUser(abortController.signal)
            .then(retrievedReserves => {
                setReserves(retrievedReserves);
                setReservesState(ReservesState.LOADED)
            })
            .catch(e => {
                if(e instanceof NotAuthorizedException) setReservesState(ReservesState.NOT_AUTHORIZED);
                else if (!(e instanceof FailedRequestException)) setReservesState(ReservesState.ERROR);
            });

        return () => abortController.abort();
    }, [ReservesState.ERROR, ReservesState.LOADED, ReservesState.NOT_AUTHORIZED]);

    if(reservesState === ReservesState.LOADING) return;

    else if (ReservesState === ReservesState.NOT_AUTHORIZED)
        return <ErrorPage errorMessage="У вас нет доступа к просмотру этой страницы."/>

    else if (ReservesState === ReservesState.ERROR)
        return <ErrorPage errorMessage="При загрузке списка зарезервированных товаров произошла ошибка."/>

    return (
        <div className="ReservesListPage">

            <div className="page-title">Список зарезервированных вами товаров:</div>

            {reserves.length === 0 &&
                <div className="page-title">У вас нет зарезервированных товаров.</div>
            }

            <div className="reserves-list">

                {reserves.map(rsrv => {
                    return (
                        <div key={"rsrv-" + rsrv["id"]} className="reserve">
                            <a href={"/reserve/" + rsrv["id"]}>
                                <div>
                                    <img src="/favicon.png" alt="favicon"/>
                                    <p>Номер {rsrv["id"]}</p>
                                </div>
                                <div>{rsrv["createdAtPresentation"].substring(0, 10)}</div>
                                <div className="reserve-status">активен</div>
                                <div className="reserve-total-price">{rsrv["totalPrice"]}₽</div>
                            </a>
                        </div>
                    );
                })}

            </div>

        </div>
    );
}

export default withHeaderAndFooter(requiresUser(
    ReservesListPage,
    "Войдите в аккаунт, чтобы просмотреть зарезервированные вами товары."
));