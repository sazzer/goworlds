import * as testSubject from './checkEmailExists';
import {expectSaga} from "redux-saga-test-plan";
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';

/** The Mock Axios layer */
const mockAxios = new MockAdapter(axios);

describe('Selectors', () => {
    describe('Empty State', () => {
        const state = {};

        it('Returns undefined for the status', () => {
            expect(testSubject.selectCheckEmailStatus(state)).toBeUndefined();
        });

        it('Returns undefined for the email value', () => {
            expect(testSubject.selectCheckEmailValue(state)).toBeUndefined();
        });
    });

    describe('Populated State', () => {
        const state = {
            checkEmailExists: {
                state: 'Started',
                email: 'graham@grahamcox.co.uk',
            }
        };

        it('Returns undefined for the status', () => {
            expect(testSubject.selectCheckEmailStatus(state)).toEqual('Started');
        });

        it('Returns undefined for the email value', () => {
            expect(testSubject.selectCheckEmailValue(state)).toEqual('graham@grahamcox.co.uk');
        });
    });
});

describe('checkEmailExists', () => {
    it('Generates the correct action', () => {
        expect(testSubject.checkEmailExists('graham@grahamcox.co.uk')).toEqual({
            type: 'CheckEmailExists/checkEmailExists',
            payload: 'graham@grahamcox.co.uk',
        });
    });

    describe('Reducers', () => {
        describe('CheckEmailExistsStartReducer', () => {
            it('Correctly works with an empty state', () => {
                const input = {
                    email: undefined,
                    state: undefined,
                };

                const result = testSubject.reducers(input, {
                    type: 'CheckEmailExists/checkEmailExists_STARTED',
                    input: 'graham@grahamcox.co.uk'
                });

                expect(result).toEqual({
                    email: 'graham@grahamcox.co.uk',
                    state: 'Started',
                });
            });

            it('Correctly works with a populated state', () => {
                const input = {
                    email: 'graham@grahamcox.co.uk',
                    state: 'Started',
                };

                const result = testSubject.reducers(input, {
                    type: 'CheckEmailExists/checkEmailExists_STARTED',
                    input: 'graham@grahamcox.co.uk'
                });

                expect(result).toEqual({
                    email: 'graham@grahamcox.co.uk',
                    state: 'Started',
                });
            });
        });

        describe('CheckEmailExistsSuccessReducer', () => {
            it('Correctly works with an empty state', () => {
                const input = {
                    email: undefined,
                    state: undefined,
                };

                const result = testSubject.reducers(input, {
                    type: 'CheckEmailExists/checkEmailExists_SUCCEEDED',
                    input: 'graham@grahamcox.co.uk',
                    payload: true
                });

                expect(result).toEqual({
                    email: 'graham@grahamcox.co.uk',
                    state: 'Exists',
                });
            });

            it('Correctly works with a populated state', () => {
                const input = {
                    email: 'graham@grahamcox.co.uk',
                    state: 'Started',
                };

                const result = testSubject.reducers(input, {
                    type: 'CheckEmailExists/checkEmailExists_SUCCEEDED',
                    input: 'graham@grahamcox.co.uk',
                    payload: false
                });

                expect(result).toEqual({
                    email: 'graham@grahamcox.co.uk',
                    state: 'Unknown',
                });
            });
        });
    });

    describe('checkEmailExistsSaga', () => {
        const action = {
            type: 'CheckEmailExists/checkEmailExists',
            payload: 'graham@grahamcox.co.uk',
        };

        it('Acts correctly when the email exists', () => {
            mockAxios.onGet('/emails/graham%40grahamcox.co.uk').reply(200, {
                exists: true
            });

            return expectSaga(testSubject.checkEmailExistsSaga, action)
                .put({
                    type: 'CheckEmailExists/checkEmailExists_STARTED',
                    input: 'graham@grahamcox.co.uk'
                })
                .put({
                    type: 'CheckEmailExists/checkEmailExists_SUCCEEDED',
                    input: 'graham@grahamcox.co.uk',
                    payload: true
                })
                .put({
                    type: 'CheckEmailExists/checkEmailExists_FINISHED',
                    input: 'graham@grahamcox.co.uk'
                })
                .run();
        });

        it('Acts correctly when the email is unknown', () => {
            mockAxios.onGet('/emails/graham%40grahamcox.co.uk').reply(200, {
                exists: false
            });

            return expectSaga(testSubject.checkEmailExistsSaga, action)
                .put({
                    type: 'CheckEmailExists/checkEmailExists_STARTED',
                    input: 'graham@grahamcox.co.uk'
                })
                .put({
                    type: 'CheckEmailExists/checkEmailExists_SUCCEEDED',
                    input: 'graham@grahamcox.co.uk',
                    payload: false
                })
                .put({
                    type: 'CheckEmailExists/checkEmailExists_FINISHED',
                    input: 'graham@grahamcox.co.uk'
                })
                .run();
        });
    });
});

describe('reset', () => {
    it('Generates the correct action', () => {
        expect(testSubject.reset()).toEqual({
            type: 'CheckEmailExists/reset',
            payload: null,
        });
    });

    describe('Reducer', () => {
        it('Correctly works with an empty state', () => {
            const input = {
                email: undefined,
                state: undefined,
            };

            const result = testSubject.reducers(input, {
                type: 'CheckEmailExists/reset',
                input: 'graham@grahamcox.co.uk'
            });

            expect(input).toEqual({
                email: undefined,
                state: undefined,
            });
            expect(result).toEqual({});
        });

        it('Correctly clears a populated state', () => {
            const input = {
                email: 'graham@grahamcox.co.uk',
                state: 'Started',
            };

            const result = testSubject.reducers(input, {
                type: 'CheckEmailExists/reset',
                input: 'graham@grahamcox.co.uk'
            });

            expect(input).toEqual({
                email: 'graham@grahamcox.co.uk',
                state: 'Started',
            });
            expect(result).toEqual({});
        });
    });
});
