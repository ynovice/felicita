import withHeader from "../hoc/withHeader";
import inCage from "../hoc/inCage";
import "../css/ProfilePage.css";
import ProxiedButton from "../components/ProxiedButton";
import $ from "jquery";
import {UserConsumer} from "../contexts/UserContext";
import React from "react";
import {SOCIAL_LOGIN_LOGOS_URLS} from "../constants";
import requiresUser from "../hoc/requiresUser";

function ProfilePage() {

    function handleCredentialDetailsExpand(e) {
        e.preventDefault();

        e.target.classList.toggle("rotated");

        const credentialId = e.target.getAttribute("data-id");
        let credentialDetails = $(`.credential-details-container[data-id=${credentialId}]`);
        credentialDetails.slideToggle(200);
    }

    return (
        <div className="ProfilePage">
            <UserConsumer>
                {userContext => {

                    const {username, oauth2Credentials} = userContext.user;

                    return (
                        <React.Fragment>
                            <div className="section">
                                <p>Привет, <span className="username">{username}</span></p>
                                <p>Это страинца твоего профиля</p>
                                <div className="links">
                                    <a href="#" className="link">Избранное</a>
                                    <a href="#" className="link">История покупок</a>
                                </div>
                            </div>
                            <div className="section">
                                <p>Учётные данные:</p>
                                <div className="credentials">

                                    {oauth2Credentials.map(credential => {

                                        const {externalId, authServer, presentation, createdAt} = credential;

                                        return (
                                            <div className="credential" key={externalId}>

                                                <div className="row">
                                                    <div className="left">
                                                        <div className="credential-name">
                                                            <img src={SOCIAL_LOGIN_LOGOS_URLS[authServer]} alt="Google"/>
                                                            <p>{authServer}</p>
                                                        </div>
                                                        <div className="credential-presentation">
                                                            {presentation}
                                                        </div>
                                                    </div>
                                                    <div className="right">
                                                        <img data-id={"0"}
                                                             src="/ui/down-arrow.png"
                                                             alt="expand"
                                                             onClick={(e) => handleCredentialDetailsExpand(e)}/>
                                                    </div>
                                                </div>

                                                <div className="credential-details-container" data-id={"0"}>
                                                    <div className="credential-details">
                                                        <p>Привязан {createdAt}</p>
                                                        <p className={"you-cant"}>Нельзя отвязать</p>
                                                    </div>
                                                </div>

                                            </div>
                                        );
                                    })}
                                </div>
                            </div>
                            <div className="section">
                                <ProxiedButton href={"/api/logout"}
                                               redirect={"/"}
                                               children={"Выйти"}
                                               style={"danger"}/>
                            </div>
                        </React.Fragment>
                    );

                }}
            </UserConsumer>
        </div>
    );
}

export default inCage(withHeader(requiresUser(
    ProfilePage, "Чтобы просмотреть эту страницу, нужно войти в аккаунт."
)));