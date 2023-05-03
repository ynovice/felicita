import {useEffect, useState} from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {UserProvider} from "./contexts/UserContext";
import {CsrfProvider} from "./contexts/CsrfContext";
import LoginPage from "./pages/LoginPage";
import "./css/App.css";
import MainPage from "./pages/MainPage";
import ProfilePage from "./pages/ProfilePage";
import Api from "./Api";
import FailedRequestException from "./exception/FailedRequestException";

function App() {

    const [userContextValue, setUserContextValue] = useState({
        user: null,
        isLoaded: false,
        hasError: false,
        errorMessage: ""
    });

    const [csrfContextValue, setCsrfContextValue] = useState({
        csrfToken: null,
        csrfHeaderName: null,
        isLoaded: false
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
                setCsrfContextValue({
                    csrfToken: csrfData.csrfToken,
                    csrfHeaderName: csrfData.csrfHeaderName,
                    isLoaded: true
                });
            })
            .catch(() => {
                setCsrfContextValue({
                    csrfToken: null,
                    csrfHeaderName: null,
                    isLoaded: true
                });
            });

        return () => abortController.abort();
    }, []);

    return (
        <UserProvider value={userContextValue}>
            <CsrfProvider value={csrfContextValue}>
                <div className="App">
                    <BrowserRouter>
                        <Routes>
                            <Route path="/" element={<MainPage /> } />
                            <Route path="/login" element={<LoginPage /> } />
                            <Route path="/profile" element={<ProfilePage /> } />
                        </Routes>
                    </BrowserRouter>
                </div>
            </CsrfProvider>
        </UserProvider>
    );
}

export default App;
