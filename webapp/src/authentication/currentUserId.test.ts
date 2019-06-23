import {reducers, storeCurrentUser} from "./currentUserId";

it('Generates the correct action', () => {
    const action = storeCurrentUser('userId');

    expect(action).toEqual({
        type: 'Authentication/storeCurrentUser',
        payload: {
            userId: 'userId'
        }
    });
});

it('Updates the state correctly when handling the action', () => {
    const initialState = {userId: undefined};

    const action = {
        type: 'Authentication/storeCurrentUser',
        payload: {
            userId: 'userId'
        }
    };

    const updated = reducers(initialState, action);

    expect(updated).toEqual({
        userId: 'userId'
    });
});

it('Doesn\'t mutate the input state when handling the action', () => {
    const initialState = {userId: undefined};

    const action = {
        type: 'Authentication/storeCurrentUser',
        payload: {
            userId: 'userId'
        }
    };

    const updated = reducers(initialState, action);

    expect(initialState).toEqual({
        userId: undefined
    });
});
