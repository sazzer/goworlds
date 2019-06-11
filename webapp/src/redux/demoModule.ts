import {createReducer} from 'redux-create-reducer';
import produce from 'immer';
import {buildSaga} from "./buildSaga";

/** The initial state */
export const initialState : any = {};

export function exampleReducer(state: any) {
    return produce(state, (draft: any) => {
        draft.hello = 'World';
    });
}

/** Demo module reducers */
export const reducers = createReducer(initialState, {
    'EXAMPLE_ACTION': exampleReducer
});

/** Demo module sagas */
export const sagas = [
    buildSaga('EXAMPLE_ACTION', function() {
        console.log('Hello');
    })
];
