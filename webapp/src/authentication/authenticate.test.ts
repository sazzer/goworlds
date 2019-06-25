import * as testSubject from "./authenticate";
import {expectSaga} from "redux-saga-test-plan";
import {buildSaga} from "../redux";
import MockAdapter from "axios-mock-adapter";
import axios from "axios";
import {OAuth2Error} from "./authenticate";

/** The Mock Axios layer */
const mockAxios = new MockAdapter(axios);

it('Generates the correct action', () => {
    const callback = jest.fn();

    expect(testSubject.authenticate('graham@grahamcox.co.uk', 'password', callback)).toEqual({
        type: 'Authentication/authenticate',
        payload: {
            email: 'graham@grahamcox.co.uk',
            password: 'password',
            callback,
        },
    });
});

describe('authenticateSaga', () => {
    it('Acts correctly when the authentication succeeds', () => {
        mockAxios.onPost('/oauth2/token').reply((config) => {
            const expectedData = new FormData();
            expectedData.set('grant_type', 'tag:goworlds,2019:oauth2/grant_type/email_password');
            expectedData.set('email', 'graham@grahamcox.co.uk');
            expectedData.set('password', 'password');
            expectedData.set('scope', 'openid');

            expect(config.data).toEqual(expectedData);
            expect(config.auth).toEqual({
                username: process.env.REACT_APP_API_CLIENTID,
                password: ''
            })

            return [200, {
                access_token: 'accessToken',
                expires_in: 36400,
                token_type: 'Bearer',
                id_token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U'
            }, {
                'content-type': 'application/json'
            }];
        });

        const callback = jest.fn();

        const action = {
            type: 'Authentication/authenticate',
            payload: {
                email: 'graham@grahamcox.co.uk',
                password: 'password',
                callback,
            },
        };

        return expectSaga(buildSaga('Authentication/authenticate', testSubject.authenticateSaga))
            .put({
                type: 'Authentication/authenticate_STARTED',
                input: {
                    email: 'graham@grahamcox.co.uk',
                    password: 'password',
                    callback,
                }
            })
            .put({
                type: 'Authentication/authenticate_SUCCEEDED',
                payload: {
                    access_token: 'accessToken',
                    expires_in: 36400,
                    token_type: 'Bearer',
                    id_token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U'
                },
                input: {
                    email: 'graham@grahamcox.co.uk',
                    password: 'password',
                    callback,
                }
            })
            .put({
                type: 'Authentication/authenticate_FINISHED',
                input: {
                    email: 'graham@grahamcox.co.uk',
                    password: 'password',
                    callback,
                }
            })
            .dispatch(action)
            .run();
    });
    it('Acts correctly when the authentication fails', () => {
        mockAxios.onPost('/oauth2/token').reply(400, {
            error: 'access_denied'
        }, {
            'content-type': 'application/json'
        });

        const callback = jest.fn();

        const action = {
            type: 'Authentication/authenticate',
            payload: {
                email: 'graham@grahamcox.co.uk',
                password: 'password',
                callback,
            },
        };

        return expectSaga(buildSaga('Authentication/authenticate', testSubject.authenticateSaga))
            .put({
                type: 'Authentication/authenticate_STARTED',
                input: {
                    email: 'graham@grahamcox.co.uk',
                    password: 'password',
                    callback,
                }
            })
            .put({
                type: 'Authentication/authenticate_FAILED',
                payload: new OAuth2Error('access_denied'),
                input: {
                    email: 'graham@grahamcox.co.uk',
                    password: 'password',
                    callback,
                }
            })
            .put({
                type: 'Authentication/authenticate_FINISHED',
                input: {
                    email: 'graham@grahamcox.co.uk',
                    password: 'password',
                    callback,
                }
            })
            .dispatch(action)
            .run();
    });
    it('Acts correctly when the network request fails', () => {
        mockAxios.onPost('/oauth2/token').networkError();

        const callback = jest.fn();

        const action = {
            type: 'Authentication/authenticate',
            payload: {
                email: 'graham@grahamcox.co.uk',
                password: 'password',
                callback,
            },
        };

        return expectSaga(buildSaga('Authentication/authenticate', testSubject.authenticateSaga))
            .put({
                type: 'Authentication/authenticate_STARTED',
                input: {
                    email: 'graham@grahamcox.co.uk',
                    password: 'password',
                    callback,
                }
            })
            .put({
                type: 'Authentication/authenticate_FAILED',
                payload: new Error('Network Error'),
                input: {
                    email: 'graham@grahamcox.co.uk',
                    password: 'password',
                    callback,
                }
            })
            .put({
                type: 'Authentication/authenticate_FINISHED',
                input: {
                    email: 'graham@grahamcox.co.uk',
                    password: 'password',
                    callback,
                }
            })
            .dispatch(action)
            .run();
    });
});

describe('authenticateFailedSaga', () => {
    it('Triggers the callback with the correct error', () => {
        const callback = jest.fn();
        const err = new Error('Network Error');

        testSubject.authenticateFailedSaga({
            type: 'Authentication/register_FAILED',
            payload: err,
            input: {
                email: 'graham@grahamcox.co.uk',
                password: 'password',
                callback,
            }
        });

        expect(callback).toBeCalledTimes(1);
        expect(callback).toBeCalledWith(err);
    });
});

describe('authenticateSucceededSaga', () => {
    it('Triggers the expected actions', async () => {
        const callback = jest.fn();

        const action = {
            type: 'Authentication/authenticate_SUCCEEDED',
            payload: {
                access_token: 'accessToken',
                expires_in: 36400,
                token_type: 'Bearer',
                // ID Token generated by jwt.io. Contains a single claim of sub=1234567890
                id_token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U'
            },
            input: {
                email: 'graham@grahamcox.co.uk',
                password: 'password',
                callback,
            }
        };

        await expectSaga(buildSaga('Authentication/authenticate_SUCCEEDED', testSubject.authenticateSucceededSaga))
            .put.like({
                action: {
                    type: 'Authentication/storeAccessToken',
                    payload: {
                        token: 'accessToken',
                        // We can't assert on the expiry date :(
                    }
                }
            })
            .put({
                type: 'Authentication/storeCurrentUser',
                payload: {
                    userId: '1234567890'
                }
            })
            .dispatch(action)
            .run();

        expect(callback).toBeCalledTimes(1);
        expect(callback).toBeCalledWith(undefined);
    });
});
