import fetchIntercept from "fetch-intercept";

export class FetchInterceptor {
    static theInstance;
    userAdaptor;
    router;
    unregister;

    constructor(session, router) {
        if (FetchInterceptor.theInstance) {
            return FetchInterceptor.theInstance;
        }

        if (!session) {
            throw new Error("Session is required for FetchInterceptor");
        }

        if (!router) {
            throw new Error("Router is required for FetchInterceptor");
        }

        this.userAdaptor = session;
        this.router = router;

        FetchInterceptor.theInstance = this;
        this.unregister = fetchIntercept.register({
            request: this.request,
            response: this.response,
            requestError: this.requestError,
            responseError: this.responseError,
        });

        console.log("FetchInterceptor has been registered for user:", this.userAdaptor.getCurrentUser);
    }

    request(url, options) {
        let token = FetchInterceptor.theInstance.userAdaptor.currentToken;

        if (token == null) {
            console.log('No token available. Requesting without token.');
            return [url, options];
        } else if (options == null) {
            console.log('Adding token to headers. Requesting with token.');
            return [url, { headers: { Authorization: token } }];
        } else {
            let newOptions = { ...options, headers: { ...options.headers, Authorization: token } };
            console.log('Adding token to existing headers. Requesting with token.');
            return [url, newOptions];
        }
    }

    response(response) {
        console.log('Received response:', response);
        FetchInterceptor.theInstance.tryRecoverNewJWToken(response);

        if (response.status >= 400 && response.status < 600) {
            FetchInterceptor.theInstance.handleErrorInResponse(response);
        }

        return response;
    }

    requestError(error) {
        console.log("FetchInterceptor requestError:", error);
        return Promise.reject(error);
    }

    responseError(error) {
        console.log("FetchInterceptor responseError:", error);
        return Promise.reject(error);
    }

    async handleErrorInResponse(response) {
        if (response.status === 401) {
            console.log('Unauthorized request. Redirecting to login.');
            this.router.push({ path: '/login' }); // vue-router
        } else if (response.status !== 406) {
            console.log('Handling other error:', response.status);
            // 406='Not Acceptable' error is used for logon failure
            // handle any other error
        }
    }

    tryRecoverNewJWToken(response) {
        const authorizationHeader = response.headers.get('Authorization');

        if (authorizationHeader && authorizationHeader.startsWith('Bearer ')) {
            const newToken = authorizationHeader.substring(7); // Extracting the token part
            console.log('Recovering new JWT token:', newToken);
            // Request the session service to save the new token
            FetchInterceptor.theInstance.userAdaptor.saveTokenIntoBrowserStorage(newToken, FetchInterceptor.theInstance.userAdaptor.getCurrentUser);
        }
    }
}
