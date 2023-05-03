import withHeader from "../hoc/withHeader";
import inCage from "../hoc/inCage";
import "../css/MainPage.css";

function MainPage() {

    return (
        <div className={"MainPage"}>
            Главная страница!
        </div>
    );
}

export default inCage(withHeader(MainPage));