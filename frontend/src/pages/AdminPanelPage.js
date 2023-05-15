import inCage from "../hoc/inCage";
import withHeader from "../hoc/withHeader";
import requiresUser from "../hoc/requiresUser";
import "../css/AdminPanelPage.css";

function AdminPanelPage() {

    return (
        <div className={"AdminPanelPage"}>

            <div className="section">
                <h1 className={"page-title"}>Панель администратора</h1>
            </div>

            <div className="section">
                <a href="/admin/item/create" className="link">Добавить новый товар</a>
            </div>

        </div>
    )
}

export default inCage(withHeader(requiresUser(
    AdminPanelPage,
    "Чтобы просмотреть эту страницу, нужно войти в аккаунт администратора.",
    true
)));