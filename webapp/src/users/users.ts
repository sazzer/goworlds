import {
    Action,
    asyncAction,
    buildAction,
    buildSaga,
    buildSelector,
    ResolvedAsyncAction,
    succeededAction
} from "../redux";
import {request} from "../api";
import {MODULE_PREFIX} from "./module";
import {createReducer} from "redux-create-reducer";
import produce from "immer";
import {select} from 'redux-saga/effects';

/** The shape of a user */
export declare type User = {
    id: string,
    created: string,
    updated: string,
    name: string,
    email: string,
};

/** The shape of the state for this module */
export declare type State = {
    users: Array<User>,
};

/** The initial state for this module */
const INITIAL_STATE: State = {
    users: [],
};

//////// Action for loading a user

/** The action type for registering a new user */
const LOAD_USER_ACTION = MODULE_PREFIX + 'loadUser';

/** The shape of the action to load a User by ID */
declare type LoadUserAction = {
    userId: string,
    force: boolean,
}

/**
 * Build the Redux action to load a user
 * @param userId The ID of the User to load
 * @param force Set to true to re-load the user even if we've already got a copy of it
 */
export function loadUser(userId: string, force: boolean = false): Action<LoadUserAction> {
    return buildAction(LOAD_USER_ACTION, {
        userId,
        force
    });
}

/**
 * Saga to load a user
 * @param action the action to handle
 */
export function* loadUserSaga(action: Action<LoadUserAction>) : IterableIterator<any> {
    let doAction = action.payload.force;
    if (!doAction) {
        const existingUser = yield select(selectUserById(action.payload.userId));
        doAction = existingUser === undefined;
    }

    if (doAction) {
        yield asyncAction(LOAD_USER_ACTION, async () => {
            const response = await request<any>('/users/{id}', {
                method: 'GET',
                urlParams: {
                    id: action.payload.userId
                }
            });

            return {
                id: action.payload.userId,
                ...response.body,
            };
        }, action.payload);
    }
}

/**
 * Reducer for storing a user
 * @param state the state to update
 * @param action the action to update the state with
 * @return the new state
 */
export function storeUserReducer(state: State, action: ResolvedAsyncAction<LoadUserAction, User>) : State {
    return produce(state, (draft: State) => {
        if (action.input === undefined || action.payload === undefined) {
            return;
        }

        const id = action.payload.id;
        const users = draft.users.filter(user => user.id !== id);

        draft.users = [
            action.payload,
            ...users,
        ];
    });
}

//////// Selectors for getting user data

/** Selector to get the specified user */
export const selectUserById = (id: string) =>
    buildSelector(['users', 'users'], (users: Array<User>) =>
        users.find(user => user.id === id));

//////// The actual module definitions

/** The sagas for this module */
export const sagas = [
    buildSaga(LOAD_USER_ACTION, loadUserSaga),
];

/** The reducers for this module */
export const reducers = createReducer(INITIAL_STATE, {
    [succeededAction(LOAD_USER_ACTION)] : storeUserReducer,
});
