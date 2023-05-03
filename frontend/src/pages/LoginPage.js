import withHeader from "../hoc/withHeader";
import inCage from "../hoc/inCage";
import "../css/LoginPage.css";

function LoginPage() {

    return (
        <div className={"LoginPage"}>

            <div className="page-title">Тебя не узнать!</div>
            <div className="second-row">Способы входа в аккаунт:</div>

            <div className="social-logos">
                <a href="http://localhost:8080/oauth2/authorization/google">
                    <img src="/logos/google.png" alt="Google"/>
                </a>
            </div>

            <div className="info-msg">Если вы ещё не создавали аккаунт, то мы создадим его для вас.</div>

        </div>
    )
}

export default inCage(withHeader(LoginPage));