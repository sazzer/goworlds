import React, {FunctionComponent} from 'react';
import {Header, Segment} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {EmailEntry} from "./EmailEntry";

/** The props that the Login area needs */
type LoginProps = {};

/**
 * The body of the login area
 * @constructor
 */
export const LoginBody: FunctionComponent<LoginProps> = () => {
    return <EmailEntry onSubmit={(email, status) => console.log(email, status)} />;
};

/**
 * The Login area
 * @constructor
 */
export const Login: FunctionComponent<LoginProps> = () => {
    const { t } = useTranslation();

    return (
        <Segment>
            <Header>
                {t('loginArea.header')}
            </Header>

            <LoginBody/>

        </Segment>
    );
};
