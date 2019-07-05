import {createReducer} from "redux-create-reducer";
import {Action, BaseAction, buildAction, buildSelector} from "../redux";
import produce from "immer";
import {MODULE_PREFIX} from "./module";
import {LOGOUT_ACTION} from "./logout";

/** The shape of the state for this module */
export declare type State = {
    userId: string | undefined,
};

/** The initial state for this module */
const INITIAL_STATE: State = {
    userId: undefined,
};

//////// Action for checking if an email address exists

/** The action type for storing the current User ID */
const STORE_CURRENT_USER_ACTION = MODULE_PREFIX + 'storeCurrentUser';

/** The shape of the action to store the current User ID */
declare type StoreCurrentUserAction = {
    userId: string,
}

/**
 * Build the Redux action to store the current User ID
 * @param userId The User ID
 */
export function storeCurrentUser(userId: string): Action<StoreCurrentUserAction> {
    return buildAction(STORE_CURRENT_USER_ACTION, {
        userId
    });
}

/**
 * Reducer for storing the current User ID
 * @param state the state to update
 * @param action the action to update the state with
 * @return the new state
 */
export function storeCurrentUserReducer(state: State, action: Action<StoreCurrentUserAction>) : State {
    return produce(state, (draft: State) => {
        draft.userId = action.payload.userId;
    });
}

/**
 * Reducer for clearing the current User ID
 * @param state the state to update
 */
export function clearCurrentUserReducer(state: State) : State {
    return produce(state, (draft: State) => {
        delete draft.userId;
    });
}

/** Selector to get the current User ID */
export const selectCurrentUserId = buildSelector(['currentUserId', 'userId']);

//////// The actual module definitions

/** The reducers for this module */
export const reducers = createReducer(INITIAL_STATE, {
    [STORE_CURRENT_USER_ACTION] : storeCurrentUserReducer as ((state: State, action: BaseAction) => State),
    [LOGOUT_ACTION] : clearCurrentUserReducer,
});
