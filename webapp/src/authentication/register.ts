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
import {put} from "redux-saga-test-plan/matchers";
import {MODULE_PREFIX} from "./module";
import {authenticate} from "./authenticate";

//////// Action for registering a new user

/** The action type for registering a new user */
const REGISTER_ACTION = MODULE_PREFIX + 'register';

/** The shape of the action to register a user */
declare type RegisterAction = {
    email: string,
    name: string,
    password: string,
    callback: (err: Error | undefined) => void
}

/**
 * Build the Redux action to register a user
 * @param email the email to register
 * @param name The name to register
 * @param password The password to register
 * @param callback A callback to trigger when the action is completed
 */
export function register(email: string, name: string, password: string, callback?: (err: Error | undefined) => void): Action<RegisterAction> {
    return buildAction(REGISTER_ACTION, {
        email,
        name,
        password,
        callback: callback || (() => {})
    });
}

/**
 * Saga to register a user
 * @param action the action to handle
 */
export function* registerSaga(action: Action<RegisterAction>) : IterableIterator<any> {
    yield asyncAction(REGISTER_ACTION, async () => {
        const response = await request<any>('/users', {
            method: 'POST',
            body: {
                name: action.payload.name,
                email: action.payload.email,
                password: action.payload.password,
            },
        });

        return response.body;
    }, action.payload);
}

/**
 * Saga to trigger when Registration fails for some reason
 * @param action the action to handle
 */
export function registerFailedSaga(action: ResolvedAsyncAction<RegisterAction, Error>) {
    if (action.input) {
        action.input.callback(action.payload);
    }
}

/**
 * Saga to trigger when Registration succeeded
 * @param action the action to handle
 */
export function* registerSucceededSaga(action: ResolvedAsyncAction<RegisterAction, any>) : IterableIterator<any> {
    if (action.input) {
        yield put(authenticate(action.input.email, action.input.password, action.input.callback));
    }
}

//////// The actual module definitions

/** The sagas for this module */
export const sagas = [
    buildSaga(REGISTER_ACTION, registerSaga),
    buildSaga(succeededAction(REGISTER_ACTION), registerSucceededSaga),
    buildSaga(failedAction(REGISTER_ACTION), registerFailedSaga),
];
