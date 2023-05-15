import {useEffect, useState} from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {UserProvider} from "./contexts/UserContext";
import LoginPage from "./pages/LoginPage";
import "./css/App.css";
import MainPage from "./pages/MainPage";
import ProfilePage from "./pages/ProfilePage";
import Api from "./Api";
import FailedRequestException from "./exception/FailedRequestException";
import AdminPanelPage from "./pages/AdminPanelPage";
import CreateItemPage from "./pages/CreateItemPage";

function App() {

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
             .then(user =>
                 setUserContextValue({
                     user,
                     isLoaded: true,
                     hasError: false
                 }))
             .catch((e) => {

                 let errorMessage = null;

                 if(e instanceof FailedRequestException) {
                     errorMessage = "Не удаётся установить соединение с сервером. Повторите попытку позже."
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
                Api.setCsrfHeaderName(csrfData.csrfHeaderName);
                Api.setCsrfToken(csrfData.csrfToken);
            });

        return () => abortController.abort();
    }, []);

    return (
        <UserProvider value={userContextValue}>
            <div className="App">
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<MainPage /> } />
                        <Route path="/login" element={<LoginPage /> } />
                        <Route path="/profile" element={<ProfilePage /> } />
                        <Route path="/admin" element={<AdminPanelPage />}/>
                        <Route path="/admin/item/create" element={<CreateItemPage />}/>
                    </Routes>
                </BrowserRouter>
            </div>
        </UserProvider>
    );
}

export default App;
