import withHeaderAndFooter from "../hoc/withHeaderAndFooter";
import "../css/LoginPage.css";
import Api from "../Api";

function LoginPage() {

    return (
        <div className={"LoginPage"}>

            <div className="page-title">Тебя не узнать!</div>

            <div className="default-login">
                <div className="seconds-row">Войти при помощи логина и пароля: </div>
                <div><input type="text" className="flct-input" placeholder="Логин"/></div>
                <div><input type="password" className="flct-input" placeholder="Пароль"/></div>
                <a href="/" className="button">Войти</a>
                <div className="second-row">Ещё не зарегистрированы? <span className="link">Регистрация</span></div>
            </div>

            <div className="second-row">Войти при помощи сторонних сервисов:</div>

            <div className="social-logos">
                <a href={Api.getServerDomain() + "/oauth2/authorization/google"}>
                    <img src="/ui/oauth/google.png" alt="Google"/>
                </a>
            </div>

            <div className="info-msg">Если вы ещё не создавали аккаунт, то мы создадим его для вас.</div>

        </div>
    )
}

export default withHeaderAndFooter(LoginPage);