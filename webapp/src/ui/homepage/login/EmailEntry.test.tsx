import React from 'react';
import {render, fireEvent} from '@testing-library/react';
import configureStore from 'redux-mock-store';
import {EmailEntry} from './EmailEntry';
import {Provider} from "react-redux";

function setup() {
    const mockStoreCreator = configureStore();
    const store = mockStoreCreator({});

    const onSubmitCallback = jest.fn();

    const element = render(<Provider store={store}><EmailEntry onSubmit={onSubmitCallback}/></Provider>);

    const enterEmail = (email: string) => {
        fireEvent.change(element.getByPlaceholderText('Enter Email Address'), {
            target: {
                value: email,
            }
        });
    };

    const submitForm = () => new Promise(resolve => {
        fireEvent.click(element.getByText('Login/Register'));
        setImmediate(resolve);
    });


    return {
        onSubmitCallback,
        store,

        element,

        enterEmail,
        submitForm
    }
}

it('Initially renders correctly', () => {
    const {element} = setup();

    expect(element.baseElement).toMatchSnapshot();
});

it('Renders correctly after entering an email address', () => {
    const {element, enterEmail} = setup();

    enterEmail('graham@grahamcox.co.uk');

    expect(element.baseElement).toMatchSnapshot();
});

it('Renders an error if submitted without an email', async () => {
    const {element, submitForm} = setup();

    await submitForm();

    expect(element.baseElement).toMatchSnapshot();
});

it('Renders as loading if submitted with an email', async () => {
    const {element, enterEmail, submitForm} = setup();

    enterEmail('graham@grahamcox.co.uk');
    await submitForm();

    expect(element.baseElement).toMatchSnapshot();
});

it('Triggers the correct Redux Action when submitted with an email', async () => {
    const {enterEmail, submitForm, store} = setup();

    enterEmail('graham@grahamcox.co.uk');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    expect(store.getActions()[0]).toMatchObject({
        type: 'CheckEmailExists/checkEmailExists',
        payload: {
            email: 'graham@grahamcox.co.uk'
        }
    });
});

it('Triggers the callback when the Redux Action completes with "true"', async () => {
    const {onSubmitCallback, enterEmail, submitForm, store} = setup();

    enterEmail('graham@grahamcox.co.uk');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(true, undefined);

    expect(onSubmitCallback).toBeCalledTimes(1);
    expect(onSubmitCallback).toBeCalledWith('graham@grahamcox.co.uk', true);
});

it('Triggers the callback when the Redux Action completes with "false"', async () => {
    const {onSubmitCallback, enterEmail, submitForm, store} = setup();

    enterEmail('graham@grahamcox.co.uk');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(false, undefined);

    expect(onSubmitCallback).toBeCalledTimes(1);
    expect(onSubmitCallback).toBeCalledWith('graham@grahamcox.co.uk', false);
});

it('Doesn\'t trigger the callback when the Redux Action indicates failure', async () => {
    const {onSubmitCallback, enterEmail, submitForm, store} = setup();

    enterEmail('graham@grahamcox.co.uk');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(false, new Error());

    expect(onSubmitCallback).toBeCalledTimes(0);
});

it('Renders an error when the Redux Action indicates failure', async () => {
    const {element, enterEmail, submitForm, store} = setup();

    enterEmail('graham@grahamcox.co.uk');
    await submitForm();

    expect(store.getActions()).toHaveLength(1);
    store.getActions()[0].payload.callback(false, new Error());

    expect(element.baseElement).toMatchSnapshot();
});
