import React from 'react';
import {ProfileBreadcrumbs} from './ProfileBreadcrumbs';
import {render} from '@testing-library/react';
import {MemoryRouter} from "react-router";

jest.mock('../common/Authenticated', () => {
    const Authenticated = jest.fn(() => 'Mock Authenticated');
    return {
        Authenticated,
    };
});

/** Set up the component to test */
function setup(name: string = 'Username') {
    return {
        element: render(<MemoryRouter><ProfileBreadcrumbs user={{name}} /></MemoryRouter>)
    };
}

it('renders without crashing', () => {
    const {element} = setup();

    expect(element.baseElement).toMatchSnapshot();
});
