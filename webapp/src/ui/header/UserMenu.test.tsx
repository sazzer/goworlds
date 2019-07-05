import React from 'react';
import {render, fireEvent} from '@testing-library/react';
import configureStore from 'redux-mock-store';
import {Provider} from 'react-redux';
import {UserMenu} from './UserMenu';
import {MemoryRouter} from "react-router";

function setup(state: any) {
    const mockStoreCreator = configureStore();
    const store = mockStoreCreator(state);

    const element = render(<Provider store={store}><MemoryRouter><UserMenu /></MemoryRouter></Provider>);

    const logout = () => new Promise(resolve => {
        fireEvent.click(element.getByText('Log Out'));
        setImmediate(resolve);
    });

    return {
        store,

        logout,

        element,
    }
}

it('Renders correctly when a user ID is available but not yet loaded', () => {
    const {element, store} = setup({
        currentUserId: {
            userId: 'abc123',
        },
        users: {
            users: []
        }
    });

    expect(element.baseElement).toMatchSnapshot();
    expect(store.getActions()).toHaveLength(1);
    expect(store.getActions()).toContainEqual(
        {
            'type': 'Users/loadUser',
            'payload': {
                'userId': 'abc123',
                'force': false,
            },
        }
    )
});

it('Renders correctly when a user ID is available and loaded', () => {
    const {element, store} = setup({
        currentUserId: {
            userId: 'abc123',
        },
        users: {
            users: [
                {
                    id: 'abc123',
                    name: 'Graham',
                }
            ]
        }
    });

    expect(element.baseElement).toMatchSnapshot();
    expect(store.getActions()).toHaveLength(0);
});

it('Dispatches the correct action when logging out', () => {
    const {logout, store} = setup({
        currentUserId: {
            userId: 'abc123',
        },
        users: {
            users: [
                {
                    id: 'abc123',
                    name: 'Graham',
                }
            ]
        }
    });

    logout();
    expect(store.getActions()).toHaveLength(1);
    expect(store.getActions()).toContainEqual({
        type: 'Authentication/logout',
        payload: {}
    });
});
