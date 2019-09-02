import React from 'react';
import {CreateWorldHeader} from './CreateWorldHeader';
import {render} from '@testing-library/react';

/** Set up the component to test */
function setup() {
    return {
        element: render(<CreateWorldHeader />)
    };
}

it('renders without crashing', () => {
    const {element} = setup();

    expect(element.baseElement).toMatchSnapshot();
});
