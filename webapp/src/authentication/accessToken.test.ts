import {reducers, storeAccessToken} from "./accessToken";

it('Generates the correct action', () => {
    const expires = new Date();
    const action = storeAccessToken('token', expires);

    expect(action).toEqual({
        type: 'AccessToken/store',
        payload: {
            token: 'token',
            expires
        }
    });
});

it('Updates the state correctly when handling the action', () => {
    const initialState = {token: undefined};

    const expires = new Date();
    const action = {
        type: 'AccessToken/store',
        payload: {
            token: 'token',
            expires
        }
    };

    const updated = reducers(initialState, action);

    expect(updated).toEqual({
        token: {
            token: 'token',
            expires
        }
    });
});

it('Doesn\'t mutate the input state when handling the action', () => {
    const initialState = {token: undefined};

    const expires = new Date();
    const action = {
        type: 'AccessToken/store',
        payload: {
            token: 'token',
            expires
        }
    };

    const updated = reducers(initialState, action);

    expect(initialState).toEqual({
        token: undefined
    });
});
