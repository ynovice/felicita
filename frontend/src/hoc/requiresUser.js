import {UserContext} from "../contexts/UserContext";
import React, {useContext} from "react";
import ErrorPage from "../pages/ErrorPage";

function requiresUser(TargetComponent, errorPageMessage, mustBeAdmin=false) {

    function RequiresUserComponent () {

        const {isLoaded, hasError, errorMessage, user} = useContext(UserContext);
        const userContextErrorMessage = errorMessage;

        if(!isLoaded) {
            return null;
        }

        if(hasError || !user || (mustBeAdmin && user["role"] !== "ADMIN")) {
            return <ErrorPage errorMessage={userContextErrorMessage ? userContextErrorMessage : errorPageMessage}/>
        }

        return <TargetComponent />
    }

    return RequiresUserComponent;
}

export default requiresUser;