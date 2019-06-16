import React from 'react';
import {shallow} from 'enzyme';
import {CheckingEmail} from "./CheckingEmail";

/** Set up the component to test */
function setup(email: string = 'graham@grahamcox.co.uk') {
    const element = shallow(<CheckingEmail email={email}/>);


    return {
        element,
    };
}

it('renders without crashing', () => {
    const {element} = setup();

    expect(element).toMatchSnapshot();
});
