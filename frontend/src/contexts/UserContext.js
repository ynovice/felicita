import React from "react";

/** @deprecated */
const UserContext = React.createContext({
    user: null,
    isLoaded: false,
    hasError: false,
    errorMessage: null
});

/** @deprecated */
const UserProvider = UserContext.Provider;
/** @deprecated */
const UserConsumer = UserContext.Consumer;

export {UserProvider, UserConsumer, UserContext};

export const UserPresenceState = {
    LOADING: "LOADING",
    PRESENT: "PRESENT",
    EMPTY: "EMPTY"
}

export const UpdatedUserContext = React.createContext({
    // must contain the following fields:
    // userPresenceState, setUserPresenceState
    // user, setUser
})

export const UpdatedUserContextProvider = UpdatedUserContext.Provider;
export const UpdatedUserContextConsumer = UpdatedUserContext.Consumer;