import RedirectionException from "./exception/RedirectionException";
import BadRequestException from "./exception/BadRequestException";
import InternalServerError from "./exception/InternalServerError";
import NotAuthorizedException from "./exception/NotAuthorizedException";
import FailedRequestException from "./exception/FailedRequestException";

const Api = (function () {

    const SERVER_DOMAIN = "http://localhost:8080";
    const API_BASE_URL = SERVER_DOMAIN + "/api";

    function throwCorrespondingException(response) {

        if(response.status === 403) {
            throw new NotAuthorizedException();
        }

        if(response.status >= 300 && response.status <= 399) {
            throw new RedirectionException();
        }

        if(response.status >= 400 && response.status <= 499) {
            throw new BadRequestException();
        }

        if(response.status >= 500 && response.status <= 599) {
            throw new InternalServerError();
        }
    }

    return {
        getCurrentUser: async function (abortSignal) {

            const response = await fetch(API_BASE_URL + "/user", {
                signal: abortSignal,
                credentials: "include"
            })
                .catch(() => {
                    return null;
                });

            if(response === null) throw new FailedRequestException()

            if(response.ok) {
                return await response.json();
            }

            throwCorrespondingException(response);
        },

        getCsrfData: async function (abortSignal) {

            const response = await fetch(API_BASE_URL + "/csrf", {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => {
                return null;
            });

            if(response === null) throw new FailedRequestException()

            if(response.ok) {
                return await response.json();
            }

            throwCorrespondingException(response);
        }
    }
})();

export default Api;