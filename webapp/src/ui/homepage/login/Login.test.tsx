import configureStore from "redux-mock-store";
import {render, fireEvent} from '@testing-library/react';
import {Provider} from "react-redux";
import React from "react";
import {Login} from "./Login";
import {OAuth2Error} from "../../../authentication/authenticate";

function setup({email} = {email: 'graham@grahamcox.co.uk'}) {
    const mockStoreCreator = configureStore();
    const store = mockStoreCreator({});

    const onCancelCallback = jest.fn();

    const element = render(<Provider store={store}><Login email={email} onCancel={onCancelCallback}/></Provider>);

    const enterPassword = (password: string) => {
        fireEvent.change(element.getByPlaceholderText('Enter Password'), {
            target: {
                value: password,
            }
        });
    };

    const submitForm = () => new Promise(resolve => {
        fireEvent.click(element.getByText('Register'));
        setImmediate(resolve);
    });

    const cancelForm = () => fireEvent.click(element.getByText('Cancel'));

    return {
        onCancelCallback,
        store,

        element,

        enterPassword,
        submitForm,
        cancelForm
    }
}

it('Initially renders correctly', () => {
    const {element} = setup();

    expect(element.baseElement).toMatchSnapshot();
});

it('Triggers the Cancel Callback when the Cancel button is pressed', () => {
    const {element, cancelForm, onCancelCallback} = setup();

    cancelForm();

    expect(onCancelCallback).toBeCalledTimes(1);
});

it('Renders correctly after entering a password', () => {
    const {element, enterPassword} = setup();

    enterPassword('password');

    expect(element.baseElement).toMatchSnapshot();
});

it('Renders an error if submitted without a password', async () => {
    const {element, submitForm} = setup();

    await submitForm();

    expect(element.baseElement).toMatchSnapshot();
});

it('Renders as loading if submitted with a password', async () => {
    const {element, enterPassword, submitForm} = setup();

    enterPassword('password');
    await submitForm();

    expect(element.baseElement).toMatchSnapshot();
});

it('Triggers the correct Redux Action when submitted with a password', async () => {
    const {enterPassword, submitForm, store} = setup();

    enterPassword('password');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    expect(store.getActions()[0]).toMatchObject({
        type: 'Authentication/authenticate',
        payload: {
            email: 'graham@grahamcox.co.uk',
            password: 'password'
        }
    });
});

it('Updates correctly when the callback indicates success', async () => {
    const {element, enterPassword, submitForm, store} = setup();

    enterPassword('password');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(undefined);

    expect(element.baseElement).toMatchSnapshot();
});

it('Updates correctly when the callback indicates an unexpected error', async () => {
    const {element, enterPassword, submitForm, store} = setup();

    enterPassword('password');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(new Error());

    expect(element.baseElement).toMatchSnapshot();
});

it('Updates correctly when the callback indicates an access_denied error', async () => {
    const {element, enterPassword, submitForm, store} = setup();

    enterPassword('password');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(new OAuth2Error('access_denied'));

    expect(element.baseElement).toMatchSnapshot();
});
