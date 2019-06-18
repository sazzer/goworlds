import React, {FunctionComponent, useState} from 'react';
import {Header, Segment} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {EmailEntry} from "./EmailEntry";
import {ExistingEmail} from "./ExistingEmail";
import {UnknownEmail} from "./UnknownEmail";

/** The props that the Login area needs */
type LoginProps = {};

/**
 * The body of the login area
 * @constructor
 */
export const LoginBody: FunctionComponent<LoginProps> = () => {
    const [email, setEmail] = useState('');
    const [emailStatus, setEmailStatus] = useState<boolean>();

    switch (emailStatus) {
        case true:
            return <ExistingEmail email={email} onCancel={() => setEmailStatus(undefined)} />;
        case false:
            return <UnknownEmail email={email} onCancel={() => setEmailStatus(undefined)} />;
        default:
            return <EmailEntry onSubmit={(email, status) => {
                setEmail(email);
                setEmailStatus(status);
            }} />;
    }


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
