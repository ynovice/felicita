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

        // return (
        //     <React.Fragment>
        //         <UserConsumer>
        //             {userContext => {
        //
        //                 if(!userContext.isLoaded) {
        //                     return null;
        //                 }
        //
        //                 if(userContext.hasError) {
        //
        //                     throw new Error("Не удаётся установить соединение с сервером, повторите попытку позднее.");
        //                 }
        //
        //                 if(!userContext.user) {
        //                     return <ErrorPage errorMessage={errorMessage} />;
        //                 }
        //
        //                 return <TargetComponent />;
        //             }}
        //         </UserConsumer>
        //     </React.Fragment>
        // );
    }

    return RequiresUserComponent;
}

export default requiresUser;