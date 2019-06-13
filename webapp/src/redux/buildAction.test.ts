import {buildAction} from "./buildAction";

it('Builds the correct structure', () => {
    const action = buildAction('ActionType', 123);

    expect(action).toEqual({
        type: 'ActionType',
        payload: 123
    });
});
