import {createReducer} from "redux-create-reducer";
import {Action, asyncAction, buildAction, buildSaga} from "../redux";

/** Prefix for actions in this module */
const MODULE_PREFIX = 'CreateUser/';

/** The shape of the state for this module */
export interface State {
}

/** The initial state for this module */
const INITIAL_STATE: State = {
};

//////// Selectors for getting details from the state

//////// Action for creating a new user

/** The action type for creating a new user */
const CREATE_USER_ACTION = MODULE_PREFIX + 'createUser';

/** The shape of the action to create a new user */
declare type CreateUserAction = {
    email: string,
    name: string,
    password: string,
};

/**
 * Build the Redux action to create a new user
 * @param email the email to user
 * @param name The name to use
 * @param password The password to use
 */
export function createUser(email: string, name: string, password: string): Action<CreateUserAction> {
    return buildAction(CREATE_USER_ACTION, {
        email,
        name,
        password,
    });
}

/** The shape of the response from the server on creating a user */
type CreateUserServiceResponse = {
};

/**
 * Saga to actually create a new user
 * @param action the action to handle
 */
export function* createUserSaga(action: Action<CreateUserAction>) : IterableIterator<any> {
    yield asyncAction(CREATE_USER_ACTION, async (email: string) => {
        return action;
    }, action.payload);
}

//////// The actual module definitions

/** The reducers for this module */
export const reducers = createReducer(INITIAL_STATE, {
});

/** The sagas for this module */
export const sagas = [
    buildSaga(CREATE_USER_ACTION, createUserSaga),
];

/** The actual module */
export default {
    createUser,
}
