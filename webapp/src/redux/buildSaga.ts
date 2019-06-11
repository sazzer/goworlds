import {takeEvery} from 'redux-saga/effects';

/**
 * Build a saga that consumes actions of a certain type and calls the given handler
 * @param type the type of action to consume
 * @param handler the handler to call
 * @return {Function} the saga
 */
export function buildSaga(type: string, handler: any) {
    return function*() {
        yield takeEvery(type, handler);
    }
}
