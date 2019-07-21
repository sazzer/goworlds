//////// Action for changing the users password

import {MODULE_PREFIX} from "./module";
import {
    Action,
    asyncAction,
    buildAction,
    buildSaga,
    failedAction,
    ResolvedAsyncAction,
    succeededAction
} from "../redux";
import {request} from "../api";
import {User} from "./user";

/** The action type for changing the users password */
export const CHANGE_PASSWORD_ACTION = MODULE_PREFIX + 'changePassword';

/** The shape of the action to change the users password */
declare type ChangePasswordAction = {
    userId: string,
    password: string,
    callback: (err: Error | undefined) => void
}

/**
 * Build the Redux action to change the users password
 * @param userId The ID of the User to update
 * @param password The new password
 * @param callback A callback to trigger when the action is completed
 */
export function changePassword(userId: string, password: string,
                                  callback?: (err: Error | undefined) => void): Action<ChangePasswordAction> {
    return buildAction(CHANGE_PASSWORD_ACTION, {
        userId,
        password,
        callback: callback || (() => {})
    });
}

/**
 * Saga to change a users password
 * @param action the action to handle
 */
export function* changePasswordSaga(action: Action<ChangePasswordAction>) : IterableIterator<any> {
    yield asyncAction(CHANGE_PASSWORD_ACTION, async () => {
        const response = await request<any>('/users/{id}', {
            method: 'PATCH',
            urlParams: {
                id: action.payload.userId
            },
            body: {
                password: action.payload.password,
            }
        });

        return {
            ...response.body,
        };
    }, action.payload);
}

/**
 * Saga for when changing a users password was a success
 * @param action the action to handle
 */
export function changePasswordSuccessSaga(action: ResolvedAsyncAction<ChangePasswordAction, User>) {
    if (action.input) {
        action.input.callback(undefined);
    }
}

/**
 * Saga for when changing a users password was a failure
 * @param action the action to handle
 */
export function changePasswordFailedSaga(action: ResolvedAsyncAction<ChangePasswordAction, Error>) {
    if (action.input) {
        action.input.callback(action.payload);
    }
}

//////// The actual module definitions

/** The sagas for this module */
export const sagas = [
    buildSaga(CHANGE_PASSWORD_ACTION, changePasswordSaga),
    buildSaga(succeededAction(CHANGE_PASSWORD_ACTION), changePasswordSuccessSaga),
    buildSaga(failedAction(CHANGE_PASSWORD_ACTION), changePasswordFailedSaga),
];
