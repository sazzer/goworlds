import {put, apply} from 'redux-saga/effects';

/** Create an action type to indicate that the async action has started */
export const startedAction = (action: string) => action + '_STARTED';

/** Create an action type to indicate that the async action has finished */
export const finishedAction = (action: string) => action + '_FINISHED';

/** Create an action type to indicate that the async action has succeeded */
export const succeededAction = (action: string) => action + '_SUCCEEDED';

/** Create an action type to indicate that the async action has failed */
export const failedAction = (action: string) => action + '_FAILED';

/** The shape of an async action */
export interface AsyncAction<I> {
    type: string,
    input?: I
}

/** The shape of a resolved async action */
export interface ResolvedAsyncAction<I, T> extends AsyncAction<I> {
    payload?: T,
}

/**
 * Execute an asynchronous action and trigger the expected events as we go
 * @param type the type of action
 * @param callback the callback to run the actual action
 * @param payload the payload to the callback
 * @return A generator that represents the action
 */
export function* asyncAction(type: string,
                                callback: (payload?: any) => any,
                                payload?: any)
    : IterableIterator<any> {

    yield put({
        type: startedAction(type),
        input: payload,
    });

    try {
        const result = yield apply(null, callback, [payload]);

        yield put({
            type: succeededAction(type),
            input: payload,
            payload: result,
        });
    } catch (error) {
        yield put({
            type: failedAction(type),
            input: payload,
            payload: error,
        });
    }

    yield put({
        type: finishedAction(type),
        input: payload,
    });
}
