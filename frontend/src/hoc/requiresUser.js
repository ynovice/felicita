import {UserContext} from "../contexts/UserContext";
import React, {useContext} from "react";
import ErrorPage from "../pages/ErrorPage";

function requiresUser(TargetComponent, errorPageMessage) {

    function RequiresUserComponent () {

        const {isLoaded, hasError, errorMessage} = useContext(UserContext);

        if(!isLoaded) {
            return null;
        }

        if(hasError) {
            return <ErrorPage errorMessage={errorMessage ? errorMessage : errorPageMessage}/>
        }

        return <TargetComponent />
    }

    return RequiresUserComponent;
}

export default requiresUser;