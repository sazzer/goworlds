import React from 'react';
import {CreateWorldBreadcrumbs} from './CreateWorldBreadcrumbs';
import {render} from '@testing-library/react';
import {MemoryRouter} from "react-router";

/** Set up the component to test */
function setup() {
    return {
        element: render(<MemoryRouter><CreateWorldBreadcrumbs /></MemoryRouter>)
    };
}

it('renders without crashing', () => {
    const {element} = setup();

    expect(element.baseElement).toMatchSnapshot();
});
