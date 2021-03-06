import {Action, buildAction, buildSaga} from "../redux";
import {request} from "../api";
import {MODULE_PREFIX} from "./module";

//////// Action for checking if an email address exists

/** The action type for checking if an email exists */
const CHECK_EMAIL_ACTION = MODULE_PREFIX + 'checkEmailExists';

/** The shape of the action to check if an email exists */
declare type CheckEmailExistsAction = {
    email: string,
    callback: (result: boolean, err: Error | undefined) => void
}

/**
 * Build the Redux action to check if an email exists
 * @param email the email to check
 * @param callback A callback to trigger when the action is completed
 */
export function checkEmailExists(email: string, callback?: (result: boolean, err: Error | undefined) => void): Action<CheckEmailExistsAction> {
    return buildAction(CHECK_EMAIL_ACTION, {
        email,
        callback: callback || (() => {})
    });
}

/** The shape of the response from the server on checking if an email address exists */
type CheckEmailExistsServiceResponse = {
    exists: boolean,
};

/**
 * Saga to check if an email address is already registered with the server
 * @param action the action to handle
 */
export function checkEmailExistsSaga(action: Action<CheckEmailExistsAction>) {
    request<CheckEmailExistsServiceResponse>('/emails/{email}', {
        method: 'GET',
        urlParams: {
            email: action.payload.email
        }
    }).then(response => {
        action.payload.callback(response.body.exists, undefined);
    }).catch(err => {
        action.payload.callback(false, err);
    });
}

//////// The actual module definitions

/** The sagas for this module */
export const sagas = [
    buildSaga(CHECK_EMAIL_ACTION, checkEmailExistsSaga),
];

/** The actual module */
export default {
    checkEmailExists
}
