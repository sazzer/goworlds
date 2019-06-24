import * as testSubject from './checkEmailExists';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';

/** The Mock Axios layer */
const mockAxios = new MockAdapter(axios);

it('Generates the correct action', () => {
    const callback = jest.fn();

    expect(testSubject.checkEmailExists('graham@grahamcox.co.uk', callback)).toEqual({
        type: 'Authentication/checkEmailExists',
        payload: {
            email: 'graham@grahamcox.co.uk',
            callback,
        },
    });
});

describe('checkEmailExistsSaga', () => {
    it('Acts correctly when the email exists', (done) => {
        mockAxios.onGet('/emails/graham%40grahamcox.co.uk').reply(200, {
            exists: true
        }, {
            'content-type': 'application/json'
        });

        const action = {
            type: 'Authentication/checkEmailExists',
            payload: {
                email: 'graham@grahamcox.co.uk',
                callback: function(status, err) {
                    expect(status).toEqual(true);
                    expect(err).toBeUndefined();
                    done();
                },
            },
        };

        testSubject.checkEmailExistsSaga(action);
    });

    it('Acts correctly when the email is unknown', (done) => {
        mockAxios.onGet('/emails/graham%40grahamcox.co.uk').reply(200, {
            exists: false
        }, {
            'content-type': 'application/json'
        });

        const action = {
            type: 'Authentication/checkEmailExists',
            payload: {
                email: 'graham@grahamcox.co.uk',
                callback: function(status, err) {
                    expect(status).toEqual(false);
                    expect(err).toBeUndefined();
                    done();
                },
            },
        };

        testSubject.checkEmailExistsSaga(action);
    });

    it('Acts correctly when the API call fails', (done) => {
        mockAxios.onGet('/emails/graham%40grahamcox.co.uk').reply(400, {
            error: 'Oops'
        }, {
            'content-type': 'application/json'
        });

        const action = {
            type: 'Authentication/checkEmailExists',
            payload: {
                email: 'graham@grahamcox.co.uk',
                callback: function(status, err) {
                    expect(err.toString()).toEqual('Error: An error occurred making a request');
                    done();
                },
            },
        };

        testSubject.checkEmailExistsSaga(action);
    });

    it('Acts correctly when the Network Request fails', (done) => {
        mockAxios.onGet('/emails/graham%40grahamcox.co.uk').networkError();

        const action = {
            type: 'Authentication/checkEmailExists',
            payload: {
                email: 'graham@grahamcox.co.uk',
                callback: function(status, err) {
                    expect(err.toString()).toEqual('Error: Network Error');
                    done();
                },
            },
        };

        testSubject.checkEmailExistsSaga(action);
    });
});

