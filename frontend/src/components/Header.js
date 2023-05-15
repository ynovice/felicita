import "../css/Header.css";
import {UserConsumer} from "../contexts/UserContext";
import {useState} from "react";
import $ from "jquery";

$(window).on('resize', function(){
    let win = $(this); //this = window
    if (win.width() < 600) {
        $(".Header .menu-container").hide();
    } else {
        $(".Header .menu-container").show();
    }
});

function Header() {

    const [mobileMenuOpened, setMobileMenuOpened] = useState(false);

    let toggleMobileMenu = function (e) {
        e.preventDefault();
        $(".Header .menu-container").slideToggle(200);
        setMobileMenuOpened(!mobileMenuOpened);
    };

    return (
        <header className={"Header"}>

            <div className="main-row">
                <div className="title">
                    <a href="/">Felicita</a>
                </div>

                <div className="menu-opener noselect"
                     onClick={(e) => toggleMobileMenu(e)}>
                    меню
                </div>

                <UserConsumer>
                    {
                        userContext => {

                            if(!userContext.isLoaded) {
                                return null;
                            }

                            if(userContext.hasError || !userContext.user) {
                                return (
                                    <div className={"menu-container"}>
                                        <ul className={"menu"}>
                                            <li className={"menu-item"}>
                                                <a href="#">Блог</a>
                                            </li>
                                            <li className={"menu-item"}>
                                                <a href="/login">Войти</a>
                                            </li>
                                        </ul>
                                    </div>
                                );
                            }

                            return (
                                <div className="menu-container">
                                    <ul className={"menu"}>
                                        <li className={"menu-item"}>
                                            <a href="#">Блог</a>
                                        </li>
                                        <li className={"menu-item"}>
                                            <a href="/profile">Профиль</a>
                                        </li>
                                        <li className={"menu-item"}>
                                            <a href="#">Корзина</a>
                                        </li>
                                    </ul>
                                </div>

                            );
                        }
                    }
                </UserConsumer>

            </div>

            <div className="links-row">
                <ul className="links-left">
                    <li><a className={"header-link"} href="#">Футболки</a></li>
                    <li><a className={"header-link"} href="#">Штанишки</a></li>
                    <li><a className={"header-link"} href="#">Платья</a></li>
                    <li><a className={"header-link"} href="#">Скидки</a></li>
                    <li><a className={"header-link"} href="#">Футболки</a></li>
                    <li><a className={"header-link"} href="#">Штанишки</a></li>
                    <li><a className={"header-link"} href="#">Платья</a></li>
                    <li><a className={"header-link"} href="#">Скидки</a></li>
                </ul>
                <ul className="links-right">
                    <li><a className={"header-link"} href="#">Каталог</a></li>
                </ul>
            </div>


        </header>
    );
}

export default Header;