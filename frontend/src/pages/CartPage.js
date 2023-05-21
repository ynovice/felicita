import withHeader from "../hoc/withHeader";
import inCage from "../hoc/inCage";
import {useContext} from "react";
import {UpdatedUserContext} from "../contexts/UserContext";
import requiresUser from "../hoc/requiresUser";
import "../css/CartPage.css";

function CartPage() {

    const {user} = useContext(UpdatedUserContext);

    return (
        <div className="CartPage">

            <div className="left-side">s</div>

            <div className="right-side"></div>

        </div>
    );
}

export default inCage(withHeader(requiresUser(
    CartPage,
    "Войдите в аккаунт, чтобы пользоваться корзиной.")));
