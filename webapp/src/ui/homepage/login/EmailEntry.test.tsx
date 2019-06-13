import React from 'react';
import {shallow} from 'enzyme';
import {EmailEntry} from "./EmailEntry";


/** Set up the component to test */
function setup() {
    const onSubmit = jest.fn();
    const element = shallow(<EmailEntry onSubmit={onSubmit}/>);

    const submitForm = () => element.find('Form').simulate('submit');
    const enterEmail = (email) => element.find('input[name="email"]').simulate('change', {
        target: {
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

it('submits the email address correctly', () => {
    const {onSubmit, enterEmail, submitForm} = setup();

    enterEmail('graham@grahamcox.co.uk');
    submitForm();

    expect(onSubmit).toBeCalledTimes(1);
    expect(onSubmit).toBeCalledWith('graham@grahamcox.co.uk');
});

it("doesn't submit the email address if one wasn't provided", () => {
    const {onSubmit, submitForm} = setup();

    submitForm();

    expect(onSubmit).toBeCalledTimes(0);
});
