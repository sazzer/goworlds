import React from 'react';
import {ProfileForm} from './ProfileForm';
import {render, fireEvent} from '@testing-library/react';
import configureStore from "redux-mock-store";
import {Provider} from "react-redux";
import {ProblemError} from "../../api";
import {User} from "../../users/user";

/** Set up the component to test */
function setup(user: User) {
    const mockStoreCreator = configureStore();
    const store = mockStoreCreator({});

    const element = render(<Provider store={store}><ProfileForm user={user} /></Provider>);

    const enterEmail = (email: string) => {
        fireEvent.change(element.getByPlaceholderText('Enter Email Address'), {
            target: {
                value: email,
            }
        });
    };

    const enterName = (email: string) => {
        fireEvent.change(element.getByPlaceholderText('Enter Name'), {
            target: {
                value: email,
            }
        });
    };

    const submitForm = () => new Promise(resolve => {
        fireEvent.click(element.getByText('Save Changes'));
        setImmediate(resolve);
    });

    const resetForm = () => new Promise(resolve => {
        fireEvent.click(element.getByText('Reset Changes'));
        setImmediate(resolve);
    });

    return {
        store,

        element,

        enterEmail,
        enterName,
        submitForm,
        resetForm
    };
}

it('renders the initial state', () => {
    const {element} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    expect(element.baseElement).toMatchSnapshot();
});

it('renders correctly after entering new details', () => {
    const {element, enterName, enterEmail} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterEmail('new@example.co.uk');
    enterName('New Name');

    expect(element.baseElement).toMatchSnapshot();
});

it('renders correctly after resetting', async () => {
    const {element, enterName, enterEmail, resetForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterEmail('new@example.co.uk');
    enterName('New Name');
    await resetForm();

    expect(element.baseElement).toMatchSnapshot();
});

it('renders errors if submitting when blank', async () => {
    const {element, enterName, enterEmail, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterEmail('');
    enterName('');
    await submitForm();

    expect(element.baseElement).toMatchSnapshot();
});

it('Marks as loading when submitting valid details', async () => {
    const {element, enterName, enterEmail, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterEmail('new@example.co.uk');
    enterName('New Name');
    await submitForm();

    expect(element.baseElement).toMatchSnapshot();
});

it('Triggers the correct redux action when submitting valid details', async () => {
    const {store, enterName, enterEmail, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterEmail('new@example.co.uk');
    enterName('New Name');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    expect(store.getActions()[0]).toMatchObject({
        type: 'Users/updateProfile',
        payload: {
            userId: 'abc123',
            name: 'New Name',
            email: 'new@example.co.uk'
        }
    });
});

it('Renders correctly when submitting triggers the callback as success', async () => {
    const {element, store, enterName, enterEmail, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterEmail('new@example.co.uk');
    enterName('New Name');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(undefined);

    expect(element.baseElement).toMatchSnapshot();
});

it('Renders correctly when submitting triggers the callback as an unexpected failure', async () => {
    const {element, store, enterName, enterEmail, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterEmail('new@example.co.uk');
    enterName('New Name');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(new Error());

    expect(element.baseElement).toMatchSnapshot();
});

it('Renders correctly when submitting triggers the callback as a duplicate email failure', async () => {
    const {element, store, enterName, enterEmail, submitForm} = setup({
        id: 'abc123',
        name: 'Test User',
        email: 'test@example.com',
        created: '',
        updated: '',
    });

    enterEmail('new@example.co.uk');
    enterName('New Name');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(new ProblemError('',
        400,
        'tag:goworlds,2019:users/problems/duplicate-email-address',
        null));

    expect(element.baseElement).toMatchSnapshot();
});