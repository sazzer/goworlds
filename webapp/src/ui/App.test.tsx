import '../i18n';

import React from 'react';
import {shallow} from 'enzyme';
import App from './App';


/** Set up the component to test */
function setup() {
    return {
        element: shallow(<App />)
    };
}

it('renders without crashing', () => {
    const {element} = setup();

    expect(element).toMatchSnapshot();
});
