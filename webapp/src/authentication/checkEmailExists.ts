import {createReducer} from "redux-create-reducer";
import produce from 'immer';
import {
    Action,
    asyncAction,
    AsyncAction,
    buildAction,
    buildSaga,
    ResolvedAsyncAction,
    startedAction,
    succeededAction
} from "../redux";

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
 * Saga to check if an email address is already registered with the server
 * @param action the action to handle
 */
export function* checkEmailExistsSaga(action: Action<CheckEmailExistsAction>) : IterableIterator<any> {
    yield asyncAction(CHECK_EMAIL_ACTION, (email: string) => {
        return (email === 'graham@grahamcox.co.uk');
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

//////// The actual module definitions

/** The reducers for this module */
export const reducers = createReducer(INITIAL_STATE, {
    [startedAction(CHECK_EMAIL_ACTION)] : CheckEmailExistsStartReducer,
    [succeededAction(CHECK_EMAIL_ACTION)] : CheckEmailExistsSuccessReducer,
});

/** The sagas for this module */
export const sagas = [
    buildSaga(CHECK_EMAIL_ACTION, checkEmailExistsSaga),
];

/** The actual module */
export default {
    checkEmailExists
}
