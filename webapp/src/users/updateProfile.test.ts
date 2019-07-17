import * as testSubject from './updateProfile';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import {expectSaga} from "redux-saga-test-plan";
import {buildSaga} from "../redux";
import {ProblemError} from "../api";

/** The Mock Axios layer */
const mockAxios = new MockAdapter(axios);

describe('updateUserProfile action', () => {
    it('Generates the correct action', () => {
        const callback = jest.fn();

        expect(testSubject.updateUserProfile('abc123', 'graham@grahamcox.co.uk', 'Graham', callback)).toEqual({
            type: 'Users/updateProfile',
            payload: {
                userId: 'abc123',
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                callback,
            },
        });
    });
});

describe('updateUserProfile saga', () => {
    const callback = jest.fn();

    const action = {
        type: 'Users/updateProfile',
        payload: {
            userId: 'abc123',
            email: 'graham@grahamcox.co.uk',
            name: 'Graham',
            callback,
        },
    };

    it('Acts correctly on success', () => {
        mockAxios.onPatch('/users/abc123').reply((config) => {
            expect(JSON.parse(config.data)).toEqual({
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
            });

            return [200, {
                id: 'abc123',
                email: 'graham@grahamcox.co.uk',
                name: 'Graham'
            }, {
                'content-type': 'application/json'
            }];
        });

        return expectSaga(buildSaga('Users/updateProfile', testSubject.updateUserProfileSaga))
            .put({
                type: 'Users/updateProfile_STARTED',
                input: {
                    userId: 'abc123',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    callback,
                },
            })
            .put({
                type: 'Users/updateProfile_SUCCEEDED',
                payload: {
                    id: 'abc123',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham'
                },
                input: {
                    userId: 'abc123',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    callback,
                },
            })
            .put({
                type: 'Users/updateProfile_FINISHED',
                input: {
                    userId: 'abc123',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    callback,
                },
            })
            .dispatch(action)
            .run();
    });

    it('Acts correctly on network error', () => {
        mockAxios.onPatch('/users/abc123').networkError();

        return expectSaga(buildSaga('Users/updateProfile', testSubject.updateUserProfileSaga))
            .put({
                type: 'Users/updateProfile_STARTED',
                input: {
                    userId: 'abc123',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    callback,
                },
            })
            .put({
                type: 'Users/updateProfile_FAILED',
                payload: new Error('Network Error'),
                input: {
                    userId: 'abc123',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    callback,
                },
            })
            .put({
                type: 'Users/updateProfile_FINISHED',
                input: {
                    userId: 'abc123',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    callback,
                },
            })
            .dispatch(action)
            .run();
    });

    it('Acts correctly on duplicate email error', () => {
        mockAxios.onPatch('/users/abc123').reply(400, {
            type: 'tag:goworlds,2019:users/problems/duplicate-email-address',
            title: 'Duplicate Email',
            status: 400,
        }, {
            'content-type': 'application/problem+json'
        });

        return expectSaga(buildSaga('Users/updateProfile', testSubject.updateUserProfileSaga))
            .put({
                type: 'Users/updateProfile_STARTED',
                input: {
                    userId: 'abc123',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    callback,
                },
            })
            .put({
                type: 'Users/updateProfile_FAILED',
                payload: new ProblemError('Duplicate Email', 400, 'tag:goworlds,2019:users/problems/duplicate-email-address'),
                input: {
                    userId: 'abc123',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    callback,
                },
            })
            .put({
                type: 'Users/updateProfile_FINISHED',
                input: {
                    userId: 'abc123',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    callback,
                },
            })
            .dispatch(action)
            .run();
    });
});

describe('updateUserProfile_SUCCEEDED saga', () => {
    it('Triggers the callback correctly', () => {
        const callback = jest.fn();

        const action = {
            type: 'Users/updateProfile_SUCCEEDED',
            payload: {
                id: 'abc123',
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                created: '',
                updated: '',
            },
            input: {
                userId: 'abc123',
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                callback,
            },
        };

        testSubject.updateUserProfileSuccessSaga(action);

        expect(callback).toBeCalledTimes(1);
        expect(callback).toBeCalledWith(undefined);
    });
});

describe('updateUserProfile_FAILED saga', () => {
    it('Triggers the callback correctly', () => {
        const callback = jest.fn();

        let problemError = new ProblemError('Duplicate Email', 400, 'tag:goworlds,2019:users/problems/duplicate-email-address');
        const action = {
            type: 'Users/updateProfile_FAILED',
            payload: problemError,
            input: {
                userId: 'abc123',
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                callback,
            },
        };

        testSubject.updateUserProfileFailedSaga(action);

        expect(callback).toBeCalledTimes(1);
        expect(callback).toBeCalledWith(problemError);
    });
});
