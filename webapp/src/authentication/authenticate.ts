import {
    Action,
    asyncAction,
    buildAction,
    buildSaga,
    failedAction,
    ResolvedAsyncAction,
    succeededAction
} from "../redux";
import {request, RequestError} from "../api";
import {put} from "redux-saga-test-plan/matchers";
import {storeAccessToken} from "./accessToken";
import jwtDecode from 'jwt-decode';
import {storeCurrentUser} from "./currentUserId";
import {MODULE_PREFIX} from "./module";
import {getConfig} from "../config";

//////// Action for authenticating to request an Access Token and ID Token

/** The action type for checking if an email exists */
const AUTHENTICATE_ACTION = MODULE_PREFIX + 'authenticate';

/** The shape of the action to authenticate a user */
declare type AuthenticateAction = {
    email: string,
    password: string,
    callback: (err: Error | undefined) => void
}

/**
 * Build the Redux action to authenticate a user
 * @param email the email to authenticate with
 * @param password The password to authenticate with
 * @param callback A callback to trigger when the action is completed
 */
export function authenticate(email: string, password: string, callback?: (err: Error | undefined) => void): Action<AuthenticateAction> {
    return buildAction(AUTHENTICATE_ACTION, {
        email,
        password,
        callback: callback || (() => {})
    });
}

/** The shape of the response from the server on requesting an access token */
declare type AuthenticationServiceResponse = {
    access_token: string,
    expires_in: number,
    token_type: string,
    id_token: string,
};

/** An Error from an OAuth2 Request */
export class OAuth2Error extends Error {
    /** The OAuth2 error code */
    errorCode: string;

    /**
     * Construct the error
     * @param error the error code
     */
    constructor(error: string) {
        super(error);

        this.errorCode = error;
    }
}

/**
 * Saga to authenticate a user
 * @param action the action to handle
 */
export function* authenticateSaga(action: Action<AuthenticateAction>) : IterableIterator<any> {
    yield asyncAction(AUTHENTICATE_ACTION, async () => {
        const requestBody = new FormData();
        requestBody.set('grant_type', 'tag:goworlds,2019:oauth2/grant_type/email_password');
        requestBody.set('email', action.payload.email);
        requestBody.set('password', action.payload.password);
        requestBody.set('scope', 'openid');

        try {
            const response = await request<AuthenticationServiceResponse>('/oauth2/token', {
                method: 'POST',
                body: requestBody,
                clientId: process.env.REACT_APP_API_CLIENTID || getConfig('API_CLIENTID'),
            });

            return response.body;
        } catch (err) {
            if (err instanceof RequestError) {
                throw new OAuth2Error(err.body.error);
            }
            throw err;
        }
    }, action.payload);
}

/**
 * Saga to trigger when Authentication fails for some reason
 * @param action the action to handle
 */
export function authenticateFailedSaga(action: ResolvedAsyncAction<AuthenticateAction, Error>) {
    if (action.input) {
        action.input.callback(action.payload);
    }
}

/**
 * Saga to trigger when Authentication succeeded
 * @param action the action to handle
 */
export function* authenticateSucceededSaga(action: ResolvedAsyncAction<AuthenticateAction, AuthenticationServiceResponse>) : IterableIterator<any> {
    if (action.payload) {
        const expiry = new Date(new Date().getTime() + (action.payload.expires_in * 1000));
        yield put(storeAccessToken(action.payload.access_token, expiry));

        const idToken = jwtDecode(action.payload.id_token) as any;
        if (idToken.sub) {
            yield put(storeCurrentUser(idToken.sub));
        }
    }

    if (action.input) {
        action.input.callback(undefined);
    }
}

//////// The actual module definitions

/** The sagas for this module */
export const sagas = [
    buildSaga(AUTHENTICATE_ACTION, authenticateSaga),
    buildSaga(succeededAction(AUTHENTICATE_ACTION), authenticateSucceededSaga),
    buildSaga(failedAction(AUTHENTICATE_ACTION), authenticateFailedSaga),
];
