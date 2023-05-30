import withHeaderAndFooter from "../hoc/withHeaderAndFooter";
import requiresUser from "../hoc/requiresUser";
import "../css/AdminPanelPage.css";
import adminAccessOnly from "../hoc/adminAccessOnly";

function AdminPanelPage() {

    return (
        <div className={"AdminPanelPage"}>

            <div className="section">
                <h1 className={"page-title"}>Панель администратора</h1>
            </div>

            <div className="section">
                <a href="/admin/item/create" className="link">Создать новый товар</a>
            </div>

            <div className="section">
                <a href="/admin/article/create" className="link">Создать статью для блога</a>
            </div>

        </div>
    )
}

export default withHeaderAndFooter(adminAccessOnly(requiresUser(
    AdminPanelPage,
    "Чтобы просмотреть эту страницу, нужно войти в аккаунт администратора."
)));