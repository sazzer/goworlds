import {applyMiddleware, combineReducers, compose, createStore} from 'redux';
import createSagaMiddleware from 'redux-saga'
import {all} from 'redux-saga/effects'
import reducers from './reducers';
import sagas from './sagas';

declare global {
    interface Window {
        __REDUX_DEVTOOLS_EXTENSION_COMPOSE__?: any;
    }
}

/** The reducer to use for the application */
const reducer = combineReducers({
    ...reducers,
});

/**
 * Actually build the store that we want to work with
 */
export function buildStore() {
    const sagaMiddleware = createSagaMiddleware();

    const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

    const store = createStore(
        reducer,
        {},
        composeEnhancers(
            applyMiddleware(
                sagaMiddleware,
            ),
        ),
    );

    sagaMiddleware.run(function* rootSaga() {
        yield all(sagas.map(saga => saga()));
    });

    return store;
}
