import "../css/ErrorPage.css";

function ErrorPage ({errorMessage = "Что-то пошло не так."}) {

    return (
        <div className="ErrorPage">
            <p className={"page-title"}>Oops :(</p>
            <p className={"error-message"}>{errorMessage}</p>
            <div className="links">
                <a href="#" className={"link"} onClick={() => window.history.back()}>☜ Взад</a>
            </div>
        </div>
    )
}

export default ErrorPage;