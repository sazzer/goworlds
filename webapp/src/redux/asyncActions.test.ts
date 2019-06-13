import * as asyncActions from './asyncActions';

describe('Building Action Names', () => {
    it('Builds a Started Action Name correctly', () => {
        const result = asyncActions.startedAction('ActionName');
        expect(result).toEqual('ActionName_STARTED');
    });
    it('Builds a Finished Action Name correctly', () => {
        const result = asyncActions.finishedAction('ActionName');
        expect(result).toEqual('ActionName_FINISHED');
    });
    it('Builds a Succeeded Action Name correctly', () => {
        const result = asyncActions.succeededAction('ActionName');
        expect(result).toEqual('ActionName_SUCCEEDED');
    });
    it('Builds a Failed Action Name correctly', () => {
        const result = asyncActions.failedAction('ActionName');
        expect(result).toEqual('ActionName_FAILED');
    });
});

describe('Executing an Async Action', () => {
    it('Reacts correctly to a successful action with no payload', () => {
        const callback = jest.fn();

        const action = asyncActions.asyncAction('ActionName', callback);

        expect(callback).toBeCalledTimes(0);
        expect(action.next()).toEqual({
            done: false,
            value: {
                combinator: false,
                payload: {
                    action: {
                        type: 'ActionName_STARTED',
                    },
                },
                type: 'PUT',
                '@@redux-saga/IO': true,
            }
        });

        expect(callback).toBeCalledTimes(0);
        expect(action.next()).toEqual({
            done: false,
            value: {
                combinator: false,
                payload: {
                    fn: callback,
                    context: null,
                    args: [
                        undefined,
                    ],
                },
                type: 'CALL',
                '@@redux-saga/IO': true,
            }
        });

        expect(callback).toBeCalledTimes(0);
        expect(action.next('Success')).toEqual({
            done: false,
            value: {
                combinator: false,
                payload: {
                    action: {
                        type: 'ActionName_SUCCEEDED',
                        payload: 'Success',
                    },
                },
                type: 'PUT',
                '@@redux-saga/IO': true,
            }
        });

        expect(callback).toBeCalledTimes(0);
        expect(action.next()).toEqual({
            done: false,
            value: {
                combinator: false,
                payload: {
                    action: {
                        type: 'ActionName_FINISHED',
                    },
                },
                type: 'PUT',
                '@@redux-saga/IO': true,
            }
        });

        expect(callback).toBeCalledTimes(0);
        expect(action.next()).toEqual({
            done: true,
        });

    });

    it('Reacts correctly to a successful action with a payload', () => {
        const callback = jest.fn();

        const action = asyncActions.asyncAction('ActionName', callback, 123);

        expect(callback).toBeCalledTimes(0);
        expect(action.next()).toEqual({
            done: false,
            value: {
                combinator: false,
                payload: {
                    action: {
                        type: 'ActionName_STARTED',
                        input: 123,
                    },
                },
                type: 'PUT',
                '@@redux-saga/IO': true,
            }
        });

        expect(callback).toBeCalledTimes(0);
        expect(action.next()).toEqual({
            done: false,
            value: {
                combinator: false,
                payload: {
                    fn: callback,
                    context: null,
                    args: [
                        123,
                    ],
                },
                type: 'CALL',
                '@@redux-saga/IO': true,
            }
        });

        expect(callback).toBeCalledTimes(0);
        expect(action.next('Success')).toEqual({
            done: false,
            value: {
                combinator: false,
                payload: {
                    action: {
                        type: 'ActionName_SUCCEEDED',
                        input: 123,
                        payload: 'Success',
                    },
                },
                type: 'PUT',
                '@@redux-saga/IO': true,
            }
        });

        expect(callback).toBeCalledTimes(0);
        expect(action.next()).toEqual({
            done: false,
            value: {
                combinator: false,
                payload: {
                    action: {
                        type: 'ActionName_FINISHED',
                        input: 123,
                    },
                },
                type: 'PUT',
                '@@redux-saga/IO': true,
            }
        });

        expect(callback).toBeCalledTimes(0);
        expect(action.next()).toEqual({
            done: true,
        });

    });

    test.todo('Reacts correctly to an unsuccessful action with no payload');

    test.todo('Reacts correctly to an unsuccessful action with a payload');
});
