import * as testSubject from './changePassword';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import {expectSaga} from "redux-saga-test-plan";
import {buildSaga} from "../redux";
import {ProblemError} from "../api";

/** The Mock Axios layer */
const mockAxios = new MockAdapter(axios);

describe('changePassword action', () => {
    it('Generates the correct action', () => {
        const callback = jest.fn();

        expect(testSubject.changePassword('abc123', 'newPassword', callback)).toEqual({
            type: 'Users/changePassword',
            payload: {
                userId: 'abc123',
                password: 'newPassword',
                callback,
            },
        });
    });
});

describe('changePassword saga', () => {
    const callback = jest.fn();

    const action = {
        type: 'Users/changePassword',
        payload: {
            userId: 'abc123',
            password: 'newPassword',
            callback,
        },
    };

    it('Acts correctly on success', () => {
        mockAxios.onPatch('/users/abc123').reply((config) => {
            expect(JSON.parse(config.data)).toEqual({
                password: 'newPassword',
            });

            return [200, {
                id: 'abc123',
                email: 'graham@grahamcox.co.uk',
                name: 'Graham'
            }, {
                'content-type': 'application/json'
            }];
        });

        return expectSaga(buildSaga('Users/changePassword', testSubject.changePasswordSaga))
            .put({
                type: 'Users/changePassword_STARTED',
                input: {
                    userId: 'abc123',
                    password: 'newPassword',
                    callback,
                },
            })
            .put({
                type: 'Users/changePassword_SUCCEEDED',
                payload: {
                    id: 'abc123',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham'
                },
                input: {
                    userId: 'abc123',
                    password: 'newPassword',
                    callback,
                },
            })
            .put({
                type: 'Users/changePassword_FINISHED',
                input: {
                    userId: 'abc123',
                    password: 'newPassword',
                    callback,
                },
            })
            .dispatch(action)
            .run();
    });

    it('Acts correctly on network error', () => {
        mockAxios.onPatch('/users/abc123').networkError();

        return expectSaga(buildSaga('Users/changePassword', testSubject.changePasswordSaga))
            .put({
                type: 'Users/changePassword_STARTED',
                input: {
                    userId: 'abc123',
                    password: 'newPassword',
                    callback,
                },
            })
            .put({
                type: 'Users/changePassword_FAILED',
                payload: new Error('Network Error'),
                input: {
                    userId: 'abc123',
                    password: 'newPassword',
                    callback,
                },
            })
            .put({
                type: 'Users/changePassword_FINISHED',
                input: {
                    userId: 'abc123',
                    password: 'newPassword',
                    callback,
                },
            })
            .dispatch(action)
            .run();
    });
});

describe('changePassword_SUCCEEDED saga', () => {
    it('Triggers the callback correctly', () => {
        const callback = jest.fn();

        const action = {
            type: 'Users/changePassword_SUCCEEDED',
            payload: {
                id: 'abc123',
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                created: '',
                updated: '',
            },
            input: {
                userId: 'abc123',
                password: 'newPassword',
                callback,
            },
        };

        testSubject.changePasswordSuccessSaga(action);

        expect(callback).toBeCalledTimes(1);
        expect(callback).toBeCalledWith(undefined);
    });
});

describe('changePassword_FAILED saga', () => {
    it('Triggers the callback correctly', () => {
        const callback = jest.fn();

        let problemError = new ProblemError('Duplicate Email', 400, 'tag:goworlds,2019:users/problems/duplicate-email-address');
        const action = {
            type: 'Users/changePassword_FAILED',
            payload: problemError,
            input: {
                userId: 'abc123',
                password: 'newPassword',
                callback,
            },
        };

        testSubject.changePasswordFailedSaga(action);

        expect(callback).toBeCalledTimes(1);
        expect(callback).toBeCalledWith(problemError);
    });
});
