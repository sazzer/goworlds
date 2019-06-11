import React from 'react';
import {Container} from "semantic-ui-react";
import HeaderBar from "./header";
import Body from './body';

/**
 * The main UI structure
 * @constructor
 */
export default function App () {
    return (
        <Container fluid>
            <HeaderBar/>
            <Body />
        </Container>
    );
}
