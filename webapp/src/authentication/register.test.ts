import MockAdapter from "axios-mock-adapter";
import axios from "axios";
import { expectSaga } from 'redux-saga-test-plan';
import * as testSubject from "./register";
import {buildSaga} from "../redux";
import {ProblemError} from "../api";

/** The Mock Axios layer */
const mockAxios = new MockAdapter(axios);

it('Generates the correct action', () => {
    const callback = jest.fn();

    expect(testSubject.register('graham@grahamcox.co.uk', 'Graham', 'password', callback)).toEqual({
        type: 'Authentication/register',
        payload: {
            email: 'graham@grahamcox.co.uk',
            name: 'Graham',
            password: 'password',
            callback,
        },
    });
});

describe('registerSaga', () => {
    it('Acts correctly when the registration succeeds', () => {
        mockAxios.onPost('/users').reply((config) => {
            expect(JSON.parse(config.data)).toEqual({
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                password: 'password',
            });

            return [
                200, {
                    response: 'here' // The actual response is ignored
                }, {
                    'content-type': 'application/json'
                }
            ]
        });

        const callback = jest.fn();

        const action = {
            type: 'Authentication/register',
            payload: {
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                password: 'password',
                callback,
            },
        };

        return expectSaga(buildSaga('Authentication/register', testSubject.registerSaga))
            .put({
                type: 'Authentication/register_STARTED',
                input: {
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    password: 'password',
                    callback,
                }
            })
            .put({
                type: 'Authentication/register_SUCCEEDED',
                payload: {
                    response: 'here'
                },
                input: {
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    password: 'password',
                    callback,
                }
            })
            .put({
                type: 'Authentication/register_FINISHED',
                input: {
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    password: 'password',
                    callback,
                }
            })
            .dispatch(action)
            .run();
    });

    describe('Acts correctly when registration fails', () => {
        it('Duplicate Email Address', () => {
            mockAxios.onPost('/users').reply(409, {
                type: 'tag:goworlds,2019:users/problems/duplicate-email-address',
                title: 'Duplicate Email Address',
                status: 409
            }, {
                'content-type': 'application/problem+json'
            });

            const callback = jest.fn();

            const action = {
                type: 'Authentication/register',
                payload: {
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    password: 'password',
                    callback,
                },
            };

            return expectSaga(buildSaga('Authentication/register', testSubject.registerSaga))
                .put({
                    type: 'Authentication/register_STARTED',
                    input: {
                        email: 'graham@grahamcox.co.uk',
                        name: 'Graham',
                        password: 'password',
                        callback,
                    }
                })
                .put({
                    type: 'Authentication/register_FAILED',
                    payload: new ProblemError('Duplicate Email Address',
                        409,
                        'tag:goworlds,2019:users/problems/duplicate-email-address',
                        {}),
                    input: {
                        email: 'graham@grahamcox.co.uk',
                        name: 'Graham',
                        password: 'password',
                        callback,
                    }
                })
                .put({
                    type: 'Authentication/register_FINISHED',
                    input: {
                        email: 'graham@grahamcox.co.uk',
                        name: 'Graham',
                        password: 'password',
                        callback,
                    }
                })
                .dispatch(action)
                .run();
        });
        it('Network Error', () => {
            mockAxios.onPost('/users').networkError();
            const callback = jest.fn();

            const action = {
                type: 'Authentication/register',
                payload: {
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    password: 'password',
                    callback,
                },
            };

            return expectSaga(buildSaga('Authentication/register', testSubject.registerSaga))
                .put({
                    type: 'Authentication/register_STARTED',
                    input: {
                        email: 'graham@grahamcox.co.uk',
                        name: 'Graham',
                        password: 'password',
                        callback,
                    }
                })
                .put({
                    type: 'Authentication/register_FAILED',
                    payload: new Error('Network Error'),
                    input: {
                        email: 'graham@grahamcox.co.uk',
                        name: 'Graham',
                        password: 'password',
                        callback,
                    }
                })
                .put({
                    type: 'Authentication/register_FINISHED',
                    input: {
                        email: 'graham@grahamcox.co.uk',
                        name: 'Graham',
                        password: 'password',
                        callback,
                    }
                })
                .dispatch(action)
                .run();
        });
    });
});

describe('registerFailedSaga', () => {
    it('Triggers the callback with the correct error', () => {
        const callback = jest.fn();
        const err = new Error('Network Error');

        testSubject.registerFailedSaga({
            type: 'Authentication/register_FAILED',
            payload: err,
            input: {
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                password: 'password',
                callback,
            }
        });

        expect(callback).toBeCalledTimes(1);
        expect(callback).toBeCalledWith(err);
    });
});

describe('registerSucceededSaga', () => {
    it('Triggers the Authenticate action', () => {
        const callback = jest.fn();

        const action = {
            type: 'Authentication/register_SUCCEEDED',
            payload: {
                response: 'here'
            },
            input: {
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                password: 'password',
                callback,
            }
        };

        return expectSaga(buildSaga('Authentication/register_SUCCEEDED', testSubject.registerSucceededSaga))
            .put({
                type: 'Authentication/authenticate',
                payload: {
                    email: 'graham@grahamcox.co.uk',
                    password: 'password',
                    callback,
                }
            })
            .dispatch(action)
            .run();
    });
});
