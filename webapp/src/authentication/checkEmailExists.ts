import {createReducer} from "redux-create-reducer";
import produce from 'immer';
import {
    Action,
    asyncAction,
    AsyncAction,
    buildAction,
    buildSaga,
    buildSelector,
    ResolvedAsyncAction,
    startedAction,
    succeededAction
} from "../redux";
import {request} from "../api";

/** Status to indicate that we've started to check if an email address exists */
export const CHECK_STATUS_START = 'Started';

/** Status to indicate that the email address we've checked does exist */
export const CHECK_STATUS_EXISTS = 'Exists';

/** Status to indicate that the email address we've checked does not exist */
export const CHECK_STATUS_UNKNOWN = 'Unknown';

/** The shape of the state for this module */
export interface State {
    email?: string,
    state?: string,
}

/** The initial state for this module */
const INITIAL_STATE: State = {

};

/** Prefix for actions in this module */
const MODULE_PREFIX = 'CheckEmailExists/';

//////// Check If Email Exists

/** The action type for checking if an email exists */
const CHECK_EMAIL_ACTION = MODULE_PREFIX + 'checkEmailExists';

/** The action type for resetting the check for if an email exists */
const RESET_ACTION = MODULE_PREFIX + 'reset';

/** The shape of the action to check if an email exists */
type CheckEmailExistsAction = string;

/**
 * Build the Redux action to check if an email exists
 * @param email the email to check
 */
export function checkEmailExists(email: string): Action<CheckEmailExistsAction> {
    return buildAction(CHECK_EMAIL_ACTION, email);
}

/**
 * Build the Redux action to check if an email exists
 */
export function reset(): Action<null> {
    return buildAction(RESET_ACTION, null);
}

/** The shape of the response from the server on checking if an email address exists */
type CheckEmailExistsServiceResponse = {
    exists: boolean,
};

/**
 * Saga to check if an email address is already registered with the server
 * @param action the action to handle
 */
export function* checkEmailExistsSaga(action: Action<CheckEmailExistsAction>) : IterableIterator<any> {
    yield asyncAction(CHECK_EMAIL_ACTION, async (email: string) => {
        const response = await request<CheckEmailExistsServiceResponse>('/emails/{email}', {
            method: 'GET',
            urlParams: {
                email,
            }
        });

        return response.body.exists;
    }, action.payload);
}

/**
 * Reducer for when we start to check if an email address exists
 * @param state the state to update
 * @param action The action to handle
 */
export function CheckEmailExistsStartReducer(state: State, action: AsyncAction<string>) : State {
    return produce(state, (draft: State) => {
        draft.email = action.input;
        draft.state = CHECK_STATUS_START;
    });
}

/**
 * Reducer for when we start to check if an email address exists
 * @param state the state to update
 * @param action The action to handle
 */
export function CheckEmailExistsSuccessReducer(state: State, action: ResolvedAsyncAction<string, boolean>) : State {
    return produce(state, (draft: State) => {
        draft.state = action.payload ? CHECK_STATUS_EXISTS : CHECK_STATUS_UNKNOWN;
    });
}

/**
 * Reducer for when we reset the check if an email address exists
 * @param state the state to update
 */
export function ResetReducer(state: State) : State {
    return produce(state, (draft: State) => {
        delete draft.state;
        delete draft.email;
    });
}

/** Selector to get the state of the Check Email process */
export const selectCheckEmailStatus = buildSelector(['checkEmailExists'], (state: State) => state.state);

/** Selector to get the Email Address we are working with */
export const selectCheckEmailValue = buildSelector(['checkEmailExists'], (state: State) => state.email);

//////// The actual module definitions

/** The reducers for this module */
export const reducers = createReducer(INITIAL_STATE, {
    [startedAction(CHECK_EMAIL_ACTION)] : CheckEmailExistsStartReducer,
    [succeededAction(CHECK_EMAIL_ACTION)] : CheckEmailExistsSuccessReducer,
    [RESET_ACTION] : ResetReducer
});

/** The sagas for this module */
export const sagas = [
    buildSaga(CHECK_EMAIL_ACTION, checkEmailExistsSaga),
];

/** The actual module */
export default {
    CHECK_STATUS_START,
    CHECK_STATUS_EXISTS,
    CHECK_STATUS_UNKNOWN,

    checkEmailExists,
    reset,

    selectCheckEmailStatus,
    selectCheckEmailValue
}
