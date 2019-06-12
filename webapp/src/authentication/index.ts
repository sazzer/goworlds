import {createReducer} from "redux-create-reducer";
import produce from 'immer';
import {buildSaga, buildAction, Action} from "../redux";

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

/** The reducers for this module */
export const reducers = createReducer(INITIAL_STATE, {

});

/** The sagas for this module */
export const sagas = [

];

/** The actual module */
export default {
    checkEmailExists
}
