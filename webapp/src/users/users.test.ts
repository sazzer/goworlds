import * as testSubject from './users';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import {expectSaga} from "redux-saga-test-plan";
import {buildSaga} from "../redux";

/** The Mock Axios layer */
const mockAxios = new MockAdapter(axios);

describe('loadUser action', () => {
    it('Generates the correct action when forcing a reload', () => {
        expect(testSubject.loadUser('abc123', true)).toEqual({
            type: 'Users/loadUser',
            payload: {
                userId: 'abc123',
                force: true,
            },
        });
    });

    it('Generates the correct action when not forcing a reload', () => {
        expect(testSubject.loadUser('abc123')).toEqual({
            type: 'Users/loadUser',
            payload: {
                userId: 'abc123',
                force: false,
            },
        });
    });
});

describe('loadUserSaga', () => {
    describe('When forcing a reload', () => {
        const action = {
            type: 'Users/loadUser',
            payload: {
                userId: 'abc123',
                force: true,
            },
        };

        it('Acts correctly if the user is cached', () => {
            mockAxios.onGet('/users/abc123').reply((config) => {
                return [200, {
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham'
                }, {
                    'content-type': 'application/json'
                }];
            });

            return expectSaga(buildSaga('Users/loadUser', testSubject.loadUserSaga))
                .withState({
                    users: {
                        users: [
                            {
                                id: 'abc123',
                            }
                        ]
                    }
                })
                .put({
                    type: 'Users/loadUser_STARTED',
                    input: {
                        userId: 'abc123',
                        force: true,
                    },
                })
                .put({
                    type: 'Users/loadUser_SUCCEEDED',
                    payload: {
                        id: 'abc123',
                        email: 'graham@grahamcox.co.uk',
                        name: 'Graham'
                    },
                    input: {
                        userId: 'abc123',
                        force: true,
                    },
                })
                .put({
                    type: 'Users/loadUser_FINISHED',
                    input: {
                        userId: 'abc123',
                        force: true,
                    },
                })
                .dispatch(action)
                .run();
        });

        it('Acts correctly if the user is not cached', () => {
            mockAxios.onGet('/users/abc123').reply((config) => {
                return [200, {
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham'
                }, {
                    'content-type': 'application/json'
                }];
            });

            return expectSaga(buildSaga('Users/loadUser', testSubject.loadUserSaga))
                .withState({
                    users: {users: []}
                })
                .put({
                    type: 'Users/loadUser_STARTED',
                    input: {
                        userId: 'abc123',
                        force: true,
                    },
                })
                .put({
                    type: 'Users/loadUser_SUCCEEDED',
                    payload: {
                        id: 'abc123',
                        email: 'graham@grahamcox.co.uk',
                        name: 'Graham'
                    },
                    input: {
                        userId: 'abc123',
                        force: true,
                    },
                })
                .put({
                    type: 'Users/loadUser_FINISHED',
                    input: {
                        userId: 'abc123',
                        force: true,
                    },
                })
                .dispatch(action)
                .run();
        });
    });

    describe('When not forcing a reload', () => {
        const action = {
            type: 'Users/loadUser',
            payload: {
                userId: 'abc123',
                force: false,
            },
        };

        it('Acts correctly if the user is cached', () => {
            return expectSaga(buildSaga('Users/loadUser', testSubject.loadUserSaga))
                .withState({
                    users: {
                        users: [
                            {
                                id: 'abc123',
                            }
                        ]
                    }
                })
                .not.put.like({
                    action: {
                        type: 'Users/loadUser_STARTED'
                    }
                })
                .not.put.like({
                    action: {
                        type: 'Users/loadUser_SUCCEEDED'
                    }
                })
                .not.put.like({
                    action: {
                        type: 'Users/loadUser_FAILED'
                    }
                })
                .not.put.like({
                    action: {
                        type: 'Users/loadUser_FINISHED'
                    }
                })
                .dispatch(action)
                .run();
        });

        it('Acts correctly if the user is not cached', () => {
            mockAxios.onGet('/users/abc123').reply((config) => {
                return [200, {
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham'
                }, {
                    'content-type': 'application/json'
                }];
            });

            return expectSaga(buildSaga('Users/loadUser', testSubject.loadUserSaga))
                .withState({
                    users: {users: []}
                })
                .put({
                    type: 'Users/loadUser_STARTED',
                    input: {
                        userId: 'abc123',
                        force: false,
                    },
                })
                .put({
                    type: 'Users/loadUser_SUCCEEDED',
                    payload: {
                        id: 'abc123',
                        email: 'graham@grahamcox.co.uk',
                        name: 'Graham'
                    },
                    input: {
                        userId: 'abc123',
                        force: false,
                    },
                })
                .put({
                    type: 'Users/loadUser_FINISHED',
                    input: {
                        userId: 'abc123',
                        force: false,
                    },
                })
                .dispatch(action)
                .run();
        });
    });
});

describe('storeUserReducer', () => {
    const action = {
        type: 'Users/loadUser_SUCCEEDED',
        payload: {
            id: 'abc123',
            email: 'graham@grahamcox.co.uk',
            name: 'Graham',
            created: '',
            updated: '',
        },
        input: {
            userId: 'abc123',
            force: false,
        },
    };

    it('Stores a user when the state is empty', () => {
        const oldState = {
            users: []
        };

        const newState = testSubject.storeUserReducer(oldState, action);

        expect(oldState).toEqual({
            users: []
        });
        expect(newState.users).toHaveLength(1);
        expect(newState.users).toContainEqual(
            {
                id: 'abc123',
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                created: '',
                updated: '',
            }
        );
    });

    it('Stores a user that\'s not been seen before', () => {
        const oldState = {
            users: [
                {
                    id: 'def321',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    created: '',
                    updated: '',
                }
            ]
        };

        const newState = testSubject.storeUserReducer(oldState, action);

        expect(oldState).toEqual({
            users: [
                {
                    id: 'def321',
                    email: 'graham@grahamcox.co.uk',
                    name: 'Graham',
                    created: '',
                    updated: '',
                }
            ]
        });
        expect(newState.users).toHaveLength(2);
        expect(newState.users).toContainEqual(
            {
                id: 'def321',
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                created: '',
                updated: '',
            }
        );
        expect(newState.users).toContainEqual(
            {
                id: 'abc123',
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                created: '',
                updated: '',
            }
        );
    });

    it('Stores a user that has been seen before', () => {
        const oldState = {
            users: [
                {
                    id: 'abc123',
                    email: 'old@grahamcox.co.uk',
                    name: 'Old',
                    created: '',
                    updated: '',
                }
            ]
        };

        const newState = testSubject.storeUserReducer(oldState, action);

        expect(oldState).toEqual({
            users: [
                {
                    id: 'abc123',
                    email: 'old@grahamcox.co.uk',
                    name: 'Old',
                    created: '',
                    updated: '',
                }
            ]
        });
        expect(newState.users).toHaveLength(1);
        expect(newState.users).toContainEqual(
            {
                id: 'abc123',
                email: 'graham@grahamcox.co.uk',
                name: 'Graham',
                created: '',
                updated: '',
            }
        );
    });
});
