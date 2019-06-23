import {createReducer} from "redux-create-reducer";
import {Action, BaseAction, buildAction} from "../redux";
import produce from "immer";
import {MODULE_PREFIX} from "./module";

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

//////// The actual module definitions

/** The reducers for this module */
export const reducers = createReducer(INITIAL_STATE, {
    [STORE_ACCESS_TOKEN_ACTION] : storeAccessTokenReducer as ((state: State, action: BaseAction) => State)
});
