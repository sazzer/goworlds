import configureStore from "redux-mock-store";
import {render} from "@testing-library/react";
import {Provider} from "react-redux";
import React from "react";
import {Authenticated, IsAuthenticated} from "./Authenticated";

function setup(userId?: string) {
    const mockStoreCreator = configureStore();
    const store = mockStoreCreator({
        currentUserId: {
            userId,
        }
    });

    const element = render(<Provider store={store}><IsAuthenticated render={auth => 'Hello: ' + auth}/></Provider>);

    return {
        store,

        element,
    }
}

it('Renders correctly when authenticated', () => {
    const {element} = setup('userId');

    expect(element.baseElement).toMatchSnapshot();
});

it('Renders correctly when not authenticated', () => {
    const {element} = setup();

    expect(element.baseElement).toMatchSnapshot();
});
