import {createReducer} from "redux-create-reducer";
import produce from 'immer';
import {buildSaga, buildAction, Action, asyncAction} from "../redux";

/** The shape of the state for this module */
export type State = {

};

/** The initial state for this module */
const INITIAL_STATE: State = {

};

/** Prefix for actions in this module */
const MODULE_PREFIX = 'Authentication/';

//////// Check If Email Exists

const CHECK_EMAIL_ACTION = MODULE_PREFIX + 'checkEmailExists';

/** The shape of the action to check if an email exists */
type CheckEmailExistsAction = {
    email: string
};

/**
 * Build the Redux action to check if an email exists
 * @param email the email to check
 */
export function checkEmailExists(email: string): Action<CheckEmailExistsAction> {
    return buildAction(CHECK_EMAIL_ACTION, {
        email
    });
}

/**
 * Saga to check if an email address is already registered with the server
 * @param action the action to handle
 */
export function* checkEmailExistsSaga(action: Action<CheckEmailExistsAction>) : IterableIterator<any> {
    yield asyncAction(CHECK_EMAIL_ACTION, (email: string) => {
        return new Promise<string>((resolve) => {
            setTimeout(() => resolve('Success: ' + email), 5000);
        });
    }, action.payload.email);
}

/** The reducers for this module */
export const reducers = createReducer(INITIAL_STATE, {

});

/** The sagas for this module */
export const sagas = [
    buildSaga(CHECK_EMAIL_ACTION, checkEmailExistsSaga),
];

/** The actual module */
export default {
    checkEmailExists
}
