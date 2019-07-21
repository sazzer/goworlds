import React from 'react';
import {PasswordForm} from './PasswordForm';
import {fireEvent, render} from '@testing-library/react';
import configureStore from "redux-mock-store";
import {Provider} from "react-redux";
import {User} from "../../users/user";

/** Set up the component to test */
function setup(user: User) {
    const mockStoreCreator = configureStore();
    const store = mockStoreCreator({});

    const element = render(<Provider store={store}><PasswordForm user={user} /></Provider>);

    const enterPassword = (value: string) => {
        fireEvent.change(element.getByPlaceholderText('Enter Password'), {
            target: {
                value,
            }
        });
    };

    const reenterPassword = (value: string) => {
        fireEvent.change(element.getByPlaceholderText('Re-enter Password'), {
            target: {
                value,
            }
        });
    };

    const submitForm = () => new Promise(resolve => {
        fireEvent.click(element.getByText('Change Password'));
        setImmediate(resolve);
    });

    return {
        store,

        element,

        enterPassword,
        reenterPassword,
        submitForm,
    };
}

it('renders the initial state when a user is present', () => {
    const {element} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    expect(element.baseElement).toMatchSnapshot();
});

it('renders correctly after entering new matching details', () => {
    const {element, enterPassword, reenterPassword} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterPassword('newPassword');
    reenterPassword('newPassword');

    expect(element.baseElement).toMatchSnapshot();
});

it('renders correctly after entering new mismatched details', () => {
    const {element, enterPassword, reenterPassword} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterPassword('newPassword');
    reenterPassword('different');

    expect(element.baseElement).toMatchSnapshot();
});

it('renders errors if submitting when blank', async () => {
    const {element, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    await submitForm();

    expect(element.baseElement).toMatchSnapshot();
});

it('Marks as loading when submitting valid details', async () => {
    const {element, enterPassword, reenterPassword, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterPassword('newPassword');
    reenterPassword('newPassword');
    await submitForm();

    expect(element.baseElement).toMatchSnapshot();
});

it('Doesn\'t mark as loading when submitting mismatched details', async () => {
    const {element, enterPassword, reenterPassword, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterPassword('newPassword');
    reenterPassword('different');
    await submitForm();

    expect(element.baseElement).toMatchSnapshot();
});

it('Triggers the correct redux action when submitting valid details', async () => {
    const {store, enterPassword, reenterPassword, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    store.clearActions();

    enterPassword('newPassword');
    reenterPassword('newPassword');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    expect(store.getActions()[0]).toMatchObject({
        type: 'Users/changePassword',
        payload: {
            userId: 'abc123',
            password: 'newPassword'
        }
    });
});

it('Doesn\'t trigger any redux action when submitting mismatched details', async () => {
    const {store, enterPassword, reenterPassword, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    store.clearActions();

    enterPassword('newPassword');
    reenterPassword('different');
    await submitForm();

    expect(store.getActions()).toHaveLength(0);
});

it('Renders correctly when submitting triggers the callback as success', async () => {
    const {element, store, enterPassword, reenterPassword, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    store.clearActions();

    enterPassword('newPassword');
    reenterPassword('newPassword');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(undefined);

    expect(element.baseElement).toMatchSnapshot();
});

it('Renders correctly when submitting triggers the callback as an unexpected failure', async () => {
    const {element, store, enterPassword, reenterPassword, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    store.clearActions();

    enterPassword('newPassword');
    reenterPassword('newPassword');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(new Error());

    expect(element.baseElement).toMatchSnapshot();
});