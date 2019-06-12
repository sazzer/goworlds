import React, {FunctionComponent} from 'react';
import {Container} from "semantic-ui-react";
import {HeaderBar} from "./header";
import {Body} from './body';
import {HomePage} from './homepage';

/** The props that an App needs */
type AppProps = {};

/**
 * The main UI structure
 * @constructor
 */
export const App: FunctionComponent<AppProps> = () => {
    return (
        <Container fluid>
            <HeaderBar/>
            <Body>
                <HomePage />
            </Body>
        </Container>
    );
};
