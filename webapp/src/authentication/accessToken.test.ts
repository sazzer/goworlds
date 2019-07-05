import {reducers, storeAccessToken} from "./accessToken";

it('Generates the correct action', () => {
    const expires = new Date();
    const action = storeAccessToken('token', expires);

    expect(action).toEqual({
        type: 'Authentication/storeAccessToken',
        payload: {
            token: 'token',
            expires
        }
    });
});

describe('Authentication/storeAccessToken', () => {
    const expires = new Date();
    const action = {
        type: 'Authentication/storeAccessToken',
        payload: {
            token: 'token',
            expires
        }
    };

    const initialState = {token: undefined};

    it('Updates the state correctly when handling the action', () => {

        const updated = reducers(initialState, action);

        expect(updated).toEqual({
            token: {
                token: 'token',
                expires
            }
        });
    });

    it('Doesn\'t mutate the input state when handling the action', () => {
        reducers(initialState, action);

        expect(initialState).toEqual({
            token: undefined
        });
    });
});

describe('Authentication/logout', () => {
    const action = {
        type: 'Authentication/logout',
        payload: {}
    };

    const expires = new Date();
    const initialState = {
        token: {
            token: 'token',
            expires,
        }
    };

    it('Updates the state correctly when handling the action', () => {

        const action = {
            type: 'Authentication/logout',
            payload: {}
        };

        const updated = reducers(initialState, action);

        expect(updated).toEqual({
            token: undefined
        });
    });

    it('Doesn\'t mutate the input state when handling the action', () => {
        reducers(initialState, action);

        expect(initialState).toEqual({
            token: {
                token: 'token',
                expires,
            }
        });
    });
});
