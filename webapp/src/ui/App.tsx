import React from 'react';
import {Container} from "semantic-ui-react";
import { useTranslation } from 'react-i18next';
import {Route, Link} from 'react-router-dom';

/**
 * The main UI structure
 * @constructor
 */
export default function App () {
    const { t } = useTranslation();

    return (
        <Container>
            <Route path="/" exact render={props => (
                <Link to="/hello">Hi there</Link>
            )} />
            <Route path="/hello" exact render={props => (
                <div>{t('hello')}</div>
            )} />
        </Container>
    );
}
