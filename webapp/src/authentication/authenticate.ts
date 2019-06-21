import {createReducer} from "redux-create-reducer";
import {Action, buildAction, buildSaga} from "../redux";

/** Prefix for actions in this module */
const MODULE_PREFIX = 'Authenticate/';

/** The shape of the state for this module */
export interface State {}

/** The initial state for this module */
const INITIAL_STATE: State = {};

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

/**
 * Saga to authenticate a user
 * @param action the action to handle
 */
export function authenticateSaga(action: Action<AuthenticateAction>) {
    console.log(action);
    action.payload.callback(new Error());
}

//////// The actual module definitions

/** The reducers for this module */
export const reducers = createReducer(INITIAL_STATE, {});

/** The sagas for this module */
export const sagas = [
    buildSaga(AUTHENTICATE_ACTION, authenticateSaga),
];

/** The actual module */
export default {
    authenticate
}
