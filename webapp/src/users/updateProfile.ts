//////// Action for updating the user profile

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

/** The action type for updating the users profile */
export const UPDATE_PROFILE_ACTION = MODULE_PREFIX + 'updateProfile';

/** The shape of the action to update the users profile */
declare type UpdateUserProfileAction = {
    userId: string,
    email: string,
    name: string,
    callback: (err: Error | undefined) => void
}

/**
 * Build the Redux action to update the users profile
 * @param userId The ID of the User to update
 * @param email The new email address of the user
 * @param name The new name of the user
 * @param callback A callback to trigger when the action is completed
 */
export function updateUserProfile(userId: string, email: string, name: string,
                                  callback?: (err: Error | undefined) => void): Action<UpdateUserProfileAction> {
    return buildAction(UPDATE_PROFILE_ACTION, {
        userId,
        email,
        name,
        callback: callback || (() => {})
    });
}

/**
 * Saga to update a user profile
 * @param action the action to handle
 */
export function* updateUserProfileSaga(action: Action<UpdateUserProfileAction>) : IterableIterator<any> {
    yield asyncAction(UPDATE_PROFILE_ACTION, async () => {
        const response = await request<any>('/users/{id}', {
            method: 'PATCH',
            urlParams: {
                id: action.payload.userId
            },
            body: {
                name: action.payload.name,
                email: action.payload.email,
            }
        });

        return {
            ...response.body,
        };
    }, action.payload);
}

/**
 * Saga for when updating a user profile was a success
 * @param action the action to handle
 */
export function updateUserProfileSuccessSaga(action: ResolvedAsyncAction<UpdateUserProfileAction, User>) {
    if (action.input) {
        action.input.callback(undefined);
    }
}

/**
 * Saga for when updating a user profile was a failure
 * @param action the action to handle
 */
export function updateUserProfileFailedSaga(action: ResolvedAsyncAction<UpdateUserProfileAction, Error>) {
    if (action.input) {
        action.input.callback(action.payload);
    }
}

//////// The actual module definitions

/** The sagas for this module */
export const sagas = [
    buildSaga(UPDATE_PROFILE_ACTION, updateUserProfileSaga),
    buildSaga(succeededAction(UPDATE_PROFILE_ACTION), updateUserProfileSuccessSaga),
    buildSaga(failedAction(UPDATE_PROFILE_ACTION), updateUserProfileFailedSaga),
];
