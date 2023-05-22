import "../css/Header.css";
import {UpdatedUserContext, UserPresenceState} from "../contexts/UserContext";
import {useContext, useEffect, useState} from "react";
import $ from "jquery";
import {AccessLevel, AppContext} from "../contexts/AppContext";
import Api from "../Api";

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

    const {accessLevel} = useContext(AppContext);
    const {userPresenceState} = useContext(UpdatedUserContext);

    const [rootCategories, setRootCategories] = useState([]);

    useEffect(() => {

        const abortController = new AbortController();

        Api.getAllCategories(abortController.signal)
            .then(retrievedCategories => setRootCategories(retrievedCategories));

        return () => abortController.abort();
    }, []);

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

                <div className="menu-container">
                    <ul className="menu">

                        {accessLevel === AccessLevel.ADMIN &&
                            <li className="menu-item">
                                <a href="/admin">Админ-панель</a>
                            </li>
                        }

                        <li className="menu-item">
                            <a href="/blog">Блог</a>
                        </li>

                        {userPresenceState === UserPresenceState.EMPTY &&
                            <li className="menu-item">
                                <a href="/login">Войти</a>
                            </li>
                        }

                        {userPresenceState === UserPresenceState.PRESENT &&
                            <li className="menu-item">
                                <a href="/profile">Профиль</a>
                            </li>
                        }

                        {userPresenceState === UserPresenceState.PRESENT &&
                            <li className="menu-item">
                                <a href="/cart">Корзина</a>
                            </li>
                        }
                    </ul>
                </div>
            </div>

            <div className="links-row">
                <ul className="links-left">
                    {rootCategories.map(rootCategory => {
                            return (
                                <li key={"root-category-header-link-container-" + rootCategory["id"]}>
                                    <a className="header-link"
                                       href={"/catalog?categoriesIds=" + rootCategory["id"]}>
                                        {rootCategory["name"]}
                                    </a>
                                </li>
                            );
                        }
                    )}
                </ul>
                <ul className="links-right">
                    <li><a className={"header-link"} href="/catalog">Каталог</a></li>
                </ul>
            </div>
        </header>
    );
}

export default Header;