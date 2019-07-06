import React, {FunctionComponent} from 'react';
import {Container} from "semantic-ui-react";
import {HeaderBar} from "./header";
import {Body} from './body';
import {HomePage} from './homepage';
import {IsAuthenticated} from "./common/Authenticated";
import {Route, Switch} from "react-router";

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
                <IsAuthenticated render={auth => (
                    <Switch>
                        {!auth && <Route render={() => <HomePage/>} />}

                        {auth && <Route path="/profile" render={() => "Profile"} />}
                        {auth && <Route render={() => "Hello"} />}
                    </Switch>
                )}/>
            </Body>
        </Container>
    );
};
