import RedirectionException from "./exception/RedirectionException";
import BadRequestException from "./exception/BadRequestException";
import InternalServerError from "./exception/InternalServerError";
import NotAuthorizedException from "./exception/NotAuthorizedException";
import FailedRequestException from "./exception/FailedRequestException";
import EntityValidationResult, {FieldError} from "./EntityValidationResult";
import InvalidEntityException from "./exception/InvalidEntityException";
import NotFoundException from "./exception/NotFoundException";
import RequestAbortedException from "./exception/RequestAbortedException";

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

        if(response === null) throw new FailedRequestException();

        if(response.status === 403) throw new NotAuthorizedException();

        if(response.status === 404) throw new NotFoundException();

        if(response.status >= 300 && response.status <= 399) throw new RedirectionException();

        if(response.status >= 400 && response.status <= 499) {

            const json = await response.json();

            if(json["invalidFields"]) throw new InvalidEntityException(buildEntityValidationResult(json));

            throw new BadRequestException();
        }

        if(response.status >= 500 && response.status <= 599) throw new InternalServerError();
    }

    return {

        logout: async function() {

            const response = await fetch(API_BASE_URL + "/logout", {
                method: "post",
                credentials: "include",
                headers: {
                    [csrfHeaderName]: csrfToken
                }
            }).catch(() => null);

            if(!response || !response.ok) {
                await throwCorrespondingException(response);
            }
        },

        getCurrentUser: async function (abortSignal) {

            let aborted = false;

            const response = await fetch(API_BASE_URL + "/user", {
                signal: abortSignal,
                credentials: "include"
            }).catch((e) => {
                if(e.name === "AbortError") aborted = true;
                return null;
            });

            if(aborted) throw new RequestAbortedException();
            if(response === null) throw new FailedRequestException()

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        getCsrfData: async function (abortSignal) {

            let aborted = false;

            const response = await fetch(API_BASE_URL + "/csrf", {
                signal: abortSignal,
                credentials: "include"
            }).catch((e) => {
                if(e.name === "AbortError") aborted = true;
                return null;
            });

            if(aborted) throw new RequestAbortedException();
            if(response === null) throw new FailedRequestException()

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        getAllCategories: async function (abortSignal) {

            const response = await fetch(API_BASE_URL + "/category", {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => null);

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        getAllMaterials: async function (abortSignal) {

            const response = await fetch(API_BASE_URL + "/material", {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => null);

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
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

            await throwCorrespondingException(response);
        },

        getAllSizes: async function (abortSignal) {

            const response = await fetch(API_BASE_URL + "/size", {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => null);

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
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
            }).catch(() => null);

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
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
            }).catch(() => null);

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        getItemById: async function (id, abortSignal) {

            const response = await fetch(API_BASE_URL + "/item/" + id, {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => null)

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        getCart: async function (abortSignal) {

            const response = await fetch(API_BASE_URL + "/cart", {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => null)

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },


        getCartEntryByUserIdAndItemId: async function (userId, itemId, abortSignal) {

            const response = await fetch(API_BASE_URL + "/ce?" + new URLSearchParams({userId, itemId}), {
                signal: abortSignal,
                credentials: "include"
            }).catch(() => null)

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        incrementItemQuantityInCart: async function (itemId, sizeId) {

            const response = await fetch(API_BASE_URL + "/ce?" + new URLSearchParams({itemId, sizeId}), {
                credentials: "include",
                method: "post",
                headers: {
                    [csrfHeaderName]: csrfToken,
                    "Content-Type": "application/json"
                }
            }).catch(() => null);

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        decrementItemQuantityInCart: async function (itemId, sizeId) {

            const response = await fetch(API_BASE_URL + "/ce?" + new URLSearchParams({itemId, sizeId}), {
                credentials: "include",
                method: "delete",
                headers: {
                    [csrfHeaderName]: csrfToken,
                    "Content-Type": "application/json"
                }
            }).catch(() => null);

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        removeSizeQuantityFromCartEntry: async function (itemId, sizeId) {

            const response = await fetch(API_BASE_URL + "/ce/sq?" + new URLSearchParams({itemId, sizeId}), {
                credentials: "include",
                method: "delete",
                headers: {
                    [csrfHeaderName]: csrfToken,
                    "Content-Type": "application/json"
                }
            }).catch(() => null);

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        getItemsPageByFilterParams: async function (searchParams, abortSignal) {

            const response = await fetch(API_BASE_URL + "/item?" + new URLSearchParams(searchParams), {
                signal: abortSignal,
                credentials: "include",
            }).catch(() => null);

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        createArticle: async function (requestDto) {

            const response = await fetch(API_BASE_URL + "/article", {
                credentials: "include",
                method: "post",
                headers: {
                    [csrfHeaderName]: csrfToken,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestDto)
            }).catch(() => null);

            if(response && response.ok) {
                return await response.json();
            }

            await throwCorrespondingException(response);
        },

        getImageUrlByImageId: function(id) {
            return API_BASE_URL + "/image/" + id;
        },

        getImageUploadUrl: function () {
            return API_BASE_URL + "/image"
        },

        getBaseApiUrl: function () {
            return API_BASE_URL;
        },

        getServerDomain: function () {
            return SERVER_DOMAIN;
        },

        setCsrfHeaderName(name) {
            csrfHeaderName = name;
        },
        getCsrfHeaderName() {
            return csrfHeaderName;
        },

        setCsrfToken(token) {
            csrfToken = token;
        },
        getCsrfToken() {
            return csrfToken;
        }
    }
})();

export default Api;