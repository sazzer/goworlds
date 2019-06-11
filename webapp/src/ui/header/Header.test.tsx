import '../../i18n';

import React from 'react';
import {shallow} from 'enzyme';
import {HeaderBar} from "./index";


/** Set up the component to test */
function setup() {
    return {
        element: shallow(<HeaderBar />)
    };
}

it('renders without crashing', () => {
    const {element} = setup();

    expect(element).toMatchSnapshot();
});
