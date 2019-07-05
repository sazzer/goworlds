import {reducers, selectCurrentUserId, storeCurrentUser} from "./currentUserId";

it('Generates the correct action', () => {
    const action = storeCurrentUser('userId');

    expect(action).toEqual({
        type: 'Authentication/storeCurrentUser',
        payload: {
            userId: 'userId'
        }
    });
});

describe('Authentication/storeCurrentUser action', () => {
    const initialState = {userId: undefined};

    const action = {
        type: 'Authentication/storeCurrentUser',
        payload: {
            userId: 'userId'
        }
    };

    it('Updates the state correctly when handling the action', () => {
        const updated = reducers(initialState, action);

        expect(updated).toEqual({
            userId: 'userId'
        });
    });

    it('Doesn\'t mutate the input state when handling the action', () => {
        reducers(initialState, action);

        expect(initialState).toEqual({
            userId: undefined
        });
    });
});


describe('Authentication/logout action', () => {
    const initialState = {userId: 'userId'};

    const action = {
        type: 'Authentication/logout',
        payload: {}
    };

    it('Updates the state correctly when handling the action', () => {
        const updated = reducers(initialState, action);

        expect(updated).toEqual({
            userId: undefined
        });
    });

    it('Doesn\'t mutate the input state when handling the action', () => {
        reducers(initialState, action);

        expect(initialState).toEqual({
            userId: 'userId'
        });
    });
});

describe('selectCurrentUserId selector', () => {
    it('Returns the Current User ID when it\'s populated', () => {
        const state = {
            currentUserId: {
                userId: 'abc123'
            }
        };

        const result = selectCurrentUserId(state);

        expect(result).toEqual('abc123');
    });

    it('Returns nothing when it\'s not populated', () => {
        const state = {
            currentUserId: {
                userId: undefined
            }
        };

        const result = selectCurrentUserId(state);

        expect(result).toBeUndefined();
    });
});
