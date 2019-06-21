import React, {FunctionComponent, useState} from 'react';
import {Header, Segment} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {EmailEntry} from "./EmailEntry";
import {Login} from "./Login";
import {Register} from "./Register";

/**
 * The body of the Login/Register area
 * @constructor
 */
export const LoginRegisterBody: FunctionComponent<object> = () => {
    const [email, setEmail] = useState<string>('');
    const [status, setStatus] = useState<boolean>();

    const resetStatus = () => {
        setEmail('');
        setStatus(undefined);
    };

    const submitEmail = (email: string, status: boolean) => {
        setEmail(email);
        setStatus(status);
    };

    switch (status) {
        case true:
            return <Login email={email} onCancel={resetStatus}/>;
        case false:
            return <Register email={email} onCancel={resetStatus}/>;
        default:
            return <EmailEntry onSubmit={submitEmail} />;
    }
};

/**
 * Component for allowing a user to Login to an existing account or Register a new one
 * @constructor
 */
export const LoginRegister: FunctionComponent<object> = () => {
    const { t } = useTranslation();

    return (
        <Segment>
            <Header>
                {t('loginArea.header')}
            </Header>

            <LoginRegisterBody/>

        </Segment>
    );
};
