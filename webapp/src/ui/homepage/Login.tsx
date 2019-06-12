import React, {FunctionComponent} from 'react';
import {useDispatch} from 'react-redux'
import {Header, Segment} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {EmailEntry} from "./EmailEntry";
import authenticationModule from "../../authentication";

/** The props that the Login area needs */
type LoginProps = {};

/**
 * The Login area
 * @constructor
 */
export const Login: FunctionComponent<LoginProps> = () => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    return (
        <Segment>
            <Header>
                {t('loginArea.header')}
            </Header>
            <EmailEntry onSubmit={(email) => dispatch(authenticationModule.checkEmailExists(email))}/>
        </Segment>
    );
};
