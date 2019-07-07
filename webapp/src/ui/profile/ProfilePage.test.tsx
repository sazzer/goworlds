import React from 'react';
import configureStore from 'redux-mock-store';
import {render} from '@testing-library/react';
import {MemoryRouter, StaticRouter} from "react-router";
import {ProfilePage} from './index';
import {Provider} from "react-redux";

jest.mock('./ProfileHeader', () => {
    const ProfileHeader = jest.fn(({user}) => 'ProfileHeader: ' + user.name);
    return {
        ProfileHeader,
    };
});

jest.mock('./ProfileBreadcrumbs', () => {
    const ProfileBreadcrumbs = jest.fn(({user}) => 'ProfileBreadcrumbs: ' + user.name);
    return {
        ProfileBreadcrumbs,
    };
});

/** Set up the component to test */
function setup({state, location} : {state: any, location: string}) {
    const mockStoreCreator = configureStore();
    const store = mockStoreCreator(state);

    return {
        store,

        element: render(<StaticRouter location={location}><Provider store={store}><ProfilePage /></Provider></StaticRouter>)
    };
}

describe('Rendering the page', () => {
    describe('When the user is still loading', () => {
        const state = {
            currentUserId: {
                userId: 'abc123',
            },
            users: {
                users: [],
            },
        };

        it('renders the profile tab', () => {
            const {element, store} = setup({state, location: '/profile'});

            expect(element.baseElement).toMatchSnapshot();
            expect(store.getActions()).toHaveLength(1);
            expect(store.getActions()).toContainEqual(
                {
                    'type': 'Users/loadUser',
                    'payload': {
                        'userId': 'abc123',
                        'force': true,
                    },
                }
            )
        });

        it('renders the password tab', () => {
            const {element, store} = setup({state, location: '/profile/password'});

            expect(element.baseElement).toMatchSnapshot();
            expect(store.getActions()).toHaveLength(1);
            expect(store.getActions()).toContainEqual(
                {
                    'type': 'Users/loadUser',
                    'payload': {
                        'userId': 'abc123',
                        'force': true,
                    },
                }
            )
        });
    });

    describe('When the user is loaded', () => {
        const state = {
            currentUserId: {
                userId: 'abc123',
            },
            users: {
                users: [
                    {
                        id: 'abc123',
                        name: 'Graham',
                        email: 'graham@grahamcox.co.uk',
                        created: 'now',
                        updated: 'now',
                    }
                ],
            },
        };

        it('renders the profile tab', () => {
            const {element, store} = setup({state, location: '/profile'});

            expect(element.baseElement).toMatchSnapshot();
            expect(store.getActions()).toHaveLength(1);
            expect(store.getActions()).toContainEqual(
                {
                    'type': 'Users/loadUser',
                    'payload': {
                        'userId': 'abc123',
                        'force': true,
                    },
                }
            )
        });

        it('renders the password tab', () => {
            const {element, store} = setup({state, location: '/profile/password'});

            expect(element.baseElement).toMatchSnapshot();
            expect(store.getActions()).toHaveLength(1);
            expect(store.getActions()).toContainEqual(
                {
                    'type': 'Users/loadUser',
                    'payload': {
                        'userId': 'abc123',
                        'force': true,
                    },
                }
            )
        });
    });

});
