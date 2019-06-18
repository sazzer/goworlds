import React from 'react';
import {mount} from 'enzyme';
import {of} from "rxjs";
import {EmailEntry} from "./EmailEntry";
import * as mockCheckEmail from '../../../authentication/checkEmail';

jest.mock('../../../authentication/checkEmail');

/** Set up the component to test */
function setup() {
    const onSubmit = jest.fn();
    const element = mount(<EmailEntry onSubmit={onSubmit}/>);

    const submitForm = () => {
        return new Promise((resolved) => {
            element.find('form').simulate('submit', {
                preventDefault: () => {}
            });

            setImmediate(() => {
                element.update();
                resolved();
            });
        });
    };
    const enterEmail = (email) => element.find('input[name="email"]').simulate('change', {
        target: {
            name: 'email',
            value: email
        }
    });

    return {
        onSubmit,
        element,
        submitForm,
        enterEmail
    };
}

it('renders without crashing', () => {
    const {element} = setup();

    expect(element).toMatchSnapshot();
});

it('renders the email address that was entered', () => {
    const {element, enterEmail} = setup();

    enterEmail('graham@grahamcox.co.uk');

    expect(element).toMatchSnapshot();
});

it('submits the email address correctly', async () => {
    const {onSubmit, enterEmail, submitForm} = setup();

    (mockCheckEmail.checkEmail as jest.MockInstance).mockReturnValue(of(true));

    enterEmail('graham@grahamcox.co.uk');
    await submitForm();

    expect(onSubmit).toBeCalledTimes(1);
    expect(onSubmit).toBeCalledWith('graham@grahamcox.co.uk', true);
});

it("doesn't submit the email address if one wasn't provided", async () => {
    const {onSubmit, submitForm} = setup();

    await submitForm();

    expect(onSubmit).toBeCalledTimes(0);
});

it("renders an error if submitted without an email address", async () => {
    const {element, submitForm} = setup();

    await submitForm();

    expect(element).toMatchSnapshot();
});
