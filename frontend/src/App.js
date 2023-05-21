import React, {useEffect, useState} from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {UpdatedUserContextProvider, UserPresenceState, UserProvider} from "./contexts/UserContext";
import LoginPage from "./pages/LoginPage";
import "./css/App.css";
import MainPage from "./pages/MainPage";
import ProfilePage from "./pages/ProfilePage";
import Api from "./Api";
import FailedRequestException from "./exception/FailedRequestException";
import AdminPanelPage from "./pages/AdminPanelPage";
import CreateItemPage from "./pages/CreateItemPage";
import ItemPage from "./pages/ItemPage";
import RequestAbortedException from "./exception/RequestAbortedException";
import NotAuthorizedException from "./exception/NotAuthorizedException";
import {AccessLevel, AppContextProvider, ServerState} from "./contexts/AppContext";
import CatalogPage from "./pages/CatalogPage";
import CartPage from "./pages/CartPage";

function App() {

    const [accessLevel, setAccessLevel] = useState(AccessLevel.GUEST);
    const [serverState, setServerState] = useState(ServerState.UNDEFINED);

    const appContextValue = {
        accessLevel, setAccessLevel,
        serverState, setServerState
    }

    const [user, setUser] = useState(null);
    const [userPresenceState, setUserPresenceState] = useState(UserPresenceState.LOADING);

    const updatedUserContextValue = {
        user, setUser,
        userPresenceState, setUserPresenceState
    };

    useEffect(() => {

        const abortController = new AbortController();

        Api
            .getCurrentUser(abortController.signal)
            .then(user => {
                setUser(user);
                setUserPresenceState(UserPresenceState.PRESENT);

                if(user["role"] === "USER") setAccessLevel(AccessLevel.AUTHENTICATED);
                else if(user["role"] === "ADMIN") setAccessLevel(AccessLevel.ADMIN);

            })
            .catch(() => {
                setUserPresenceState(UserPresenceState.EMPTY);
            })

        return () => abortController.abort();
    }, []);

    const [userContextValue, setUserContextValue] = useState({
        user: null,
        isLoaded: false,
        hasError: false,
        errorMessage: ""
    });

    useEffect(() => {

        const abortController = new AbortController();

         Api
             .getCurrentUser(abortController.signal)
             .then(user => {
                 setUserContextValue({
                     user,
                     isLoaded: true,
                     hasError: false
                 });
             })
             .catch(e => {

                 if(e instanceof RequestAbortedException) return null;

                 let errorMessage = null;

                 if(e instanceof FailedRequestException) {
                     errorMessage = "Не удаётся установить соединение с сервером. Повторите попытку позже."
                 }

                 if(e instanceof NotAuthorizedException) {
                     errorMessage = "Вы не вошли в аккаунт"
                 }

                 setUserContextValue({
                     user: null,
                     isLoaded: true,
                     hasError: true,
                     errorMessage
                 });

             });

        return () => abortController.abort();
    }, []);

    useEffect(() => {

        const abortController = new AbortController();

        Api.getCsrfData(abortController.signal)
            .then(csrfData => {
                Api.setCsrfHeaderName(csrfData["csrfHeaderName"]);
                Api.setCsrfToken(csrfData["csrfToken"]);

                setServerState(ServerState.AVAILABLE);

            })
            .catch(e => {
                if(e instanceof RequestAbortedException) return null;
                setServerState(ServerState.UNAVAILABLE);
            });

        return () => abortController.abort();
    }, []);

    return (
        <AppContextProvider value={appContextValue}>
            <UpdatedUserContextProvider value={updatedUserContextValue}>
                <UserProvider value={userContextValue}>
                    <div className="App">
                        <BrowserRouter>
                            <Routes>
                                <Route path="/" element={<MainPage /> } />
                                <Route path="/login" element={<LoginPage /> } />
                                <Route path="/profile" element={<ProfilePage /> } />
                                <Route path="/item/:id" element={<ItemPage /> } />
                                <Route path="/catalog" element={<CatalogPage />}/>
                                <Route path="/cart" element={<CartPage />}/>
                                <Route path="/admin" element={<AdminPanelPage />}/>
                                <Route path="/admin/item/create" element={<CreateItemPage />}/>
                            </Routes>
                        </BrowserRouter>
                    </div>
                </UserProvider>
            </UpdatedUserContextProvider>
     </AppContextProvider>
    );
}

export default App;
