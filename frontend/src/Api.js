import RedirectionException from "./exception/RedirectionException";
import BadRequestException from "./exception/BadRequestException";
import InternalServerError from "./exception/InternalServerError";
import NotAuthorizedException from "./exception/NotAuthorizedException";
import FailedRequestException from "./exception/FailedRequestException";
import EntityValidationResult, {FieldError} from "./EntityValidationResult";
import InvalidEntityException from "./exception/InvalidEntityException";

const Api = (function () {

    const SERVER_DOMAIN = "http://localhost:8080";
    const API_BASE_URL = SERVER_DOMAIN + "/api";

    let csrfHeaderName = null;
    let csrfToken = null;

    const buildEntityValidationResult = (invalidEntityDto) => {

        let objectValidationResult = new EntityValidationResult();

        objectValidationResult.hasErrors = true;

        let fieldErrors = [];

        for(const index in invalidEntityDto["invalidFields"]) {
            fieldErrors.push(
                new FieldError(
                    invalidEntityDto["invalidFields"][index]["fieldName"],
                    invalidEntityDto["invalidFields"][index]["errorCode"],
                    invalidEntityDto["invalidFields"][index]["errorMessage"]
                )
            );
        }

        objectValidationResult.fieldErrors = fieldErrors;

        return objectValidationResult;
    }

    const throwCorrespondingException = async (response) => {

        if(response.status === 403) {
            throw new NotAuthorizedException();
        }

        if(response.status >= 300 && response.status <= 399) {
            throw new RedirectionException();
        }

        if(response.status >= 400 && response.status <= 499) {

            const json = await response.json();

            console.log(json)

            if(json["invalidFields"]) {
                throw new InvalidEntityException(buildEntityValidationResult(json));
            }

            throw new BadRequestException();
        }

        if(response.status >= 500 && response.status <= 599) {
            throw new InternalServerError();
        }
    }

    return {

        logout: async function() {

            const response = await fetch(API_BASE_URL + "/logout", {
                method: "post",
                credentials: "include",
                headers: {
                    [csrfHeaderName]: csrfToken
                }
            });

            return response && response.ok;
        },

        getCurrentUser: async function (abortSignal) {

            const response = await fetch(API_BASE_URL + "/user", {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => {
                    return null;
                });

            if(response === null) throw new FailedRequestException()

            if(response && response.ok) {
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

            if(response && response.ok) {
                return await response.json();
            }

            throwCorrespondingException(response);
        },

        getAllCategories: async function (abortSignal) {

            const response = await fetch(API_BASE_URL + "/category", {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => {
                return null;
            });

            if(response && response.ok) {
                return await response.json();
            }

            throwCorrespondingException(response);
        },

        getAllMaterials: async function (abortSignal) {

            const response = await fetch(API_BASE_URL + "/material", {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => {
                return null;
            });

            if(response && response.ok) {
                return await response.json();
            }

            throwCorrespondingException(response);
        },

        getAllColors: async function (abortSignal) {

            const response = await fetch(API_BASE_URL + "/color", {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => {
                return null;
            });

            if(response && response.ok) {
                return await response.json();
            }

            throwCorrespondingException(response);
        },

        getAllSizes: async function (abortSignal) {

            const response = await fetch(API_BASE_URL + "/size", {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => {
                return null;
            });

            if(response && response.ok) {
                return await response.json();
            }

            throwCorrespondingException(response);
        },

        uploadImage: async function (image, abortSignal) {

            const body = new FormData();
            body.append("image", image);

            const response = await fetch(API_BASE_URL + "/image", {
                signal: abortSignal,
                credentials: "include",
                method: "post",
                headers: {
                    [csrfHeaderName]: csrfToken
                },
                body: body
            }).catch(() => {
                return null;
            });

            if(response && response.ok) {
                return await response.json();
            }

            throwCorrespondingException(response);
        },

        createItem: async function (requestDto, abortSignal) {

            const response = await fetch(API_BASE_URL + "/item", {
                signal: abortSignal,
                credentials: "include",
                method: "post",
                headers: {
                    [csrfHeaderName]: csrfToken,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestDto)
            }).catch(() => {
                return null;
            });

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        getBaseApiUrl() {
            return API_BASE_URL;
        },

        setCsrfHeaderName(name) {
            csrfHeaderName = name;
        },

        setCsrfToken(token) {
            csrfToken = token;
        }
    }
})();

export default Api;