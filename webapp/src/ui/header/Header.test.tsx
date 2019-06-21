import React from 'react';
import {HeaderBar} from "./index";
import {render} from "@testing-library/react";


/** Set up the component to test */
function setup() {
    return {
        element: render(<HeaderBar />)
    };
}

it('renders without crashing', () => {
    const {element} = setup();

    expect(element.baseElement).toMatchSnapshot();
});
