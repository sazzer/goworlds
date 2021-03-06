import {createReducer} from "redux-create-reducer";
import {Action, BaseAction, buildAction, buildSaga} from "../redux";
import produce from "immer";
import {MODULE_PREFIX} from "./module";
import {LOGOUT_ACTION} from "./logout";
import {clearAccessToken, setAccessToken} from "../api";

/** The shape of an access token */
export declare type Token = {
    token: string,
    expires: Date,
};

/** The shape of the state for this module */
export declare type State = {
    token: Token | undefined,
};

/** The initial state for this module */
const INITIAL_STATE: State = {
    token: undefined,
};

//////// Action for checking if an email address exists

/** The action type for storing an access token */
const STORE_ACCESS_TOKEN_ACTION = MODULE_PREFIX + 'storeAccessToken';

/** The shape of the action to store an access token */
declare type StoreAccessTokenAction = {
    token: string,
    expires: Date,
}

/**
 * Build the Redux action to store an access token
 * @param token The actual token
 * @param expires When the token expires
 */
export function storeAccessToken(token: string, expires: Date): Action<StoreAccessTokenAction> {
    return buildAction(STORE_ACCESS_TOKEN_ACTION, {
        token,
        expires
    });
}

/**
 * Reducer for storing the access token
 * @param state the state to update
 * @param action the action to update the state with
 * @return the new state
 */
export function storeAccessTokenReducer(state: State, action: Action<StoreAccessTokenAction>) : State {
    return produce(state, (draft: State) => {
        draft.token = action.payload;
    });
}

/**
 * Reducer for clearing the access token
 * @param state the state to update
 */
export function clearAccessTokenReducer(state: State) : State {
    return produce(state, (draft: State) => {
        delete draft.token;
    });
}

/**
 * Saga to save an access token
 * @param action the action to handle
 */
export function saveAccessToken(action: Action<StoreAccessTokenAction>) {
    setAccessToken(action.payload.token);
}

/**
 * Saga to reset an access token
 */
export function resetAccessToken() {
    clearAccessToken();
}

//////// The actual module definitions

/** The sagas for this module */
export const sagas = [
    buildSaga(STORE_ACCESS_TOKEN_ACTION, saveAccessToken),
    buildSaga(LOGOUT_ACTION, resetAccessToken),
];

/** The reducers for this module */
export const reducers = createReducer(INITIAL_STATE, {
    [STORE_ACCESS_TOKEN_ACTION] : storeAccessTokenReducer as ((state: State, action: BaseAction) => State),
    [LOGOUT_ACTION] : clearAccessTokenReducer,
});
