import React from 'react';
import {ProfileHeader} from './ProfileHeader';
import {render} from '@testing-library/react';

jest.mock('../common/Authenticated', () => {
    const Authenticated = jest.fn(() => 'Mock Authenticated');
    return {
        Authenticated,
    };
});

/** Set up the component to test */
function setup(name: string = 'Username') {
    return {
        element: render(<ProfileHeader user={{name}} />)
    };
}

it('renders without crashing', () => {
    const {element} = setup();

    expect(element.baseElement).toMatchSnapshot();
});
