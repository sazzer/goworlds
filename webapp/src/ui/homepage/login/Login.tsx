import React, {FunctionComponent} from 'react';
import {useDispatch, useSelector} from 'react-redux'
import {Header, Segment} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {EmailEntry} from "./EmailEntry";
import checkEmailExistsModule from "../../../authentication/checkEmailExists";
import {CheckingEmail} from "./CheckingEmail";
import {ExistingEmail} from "./ExistingEmail";
import {UnknownEmail} from "./UnknownEmail";

/** The props that the Login area needs */
type LoginProps = {};

/**
 * The body of the login area
 * @constructor
 */
export const LoginBody: FunctionComponent<LoginProps> = () => {
    const dispatch = useDispatch();
    const checkEmailStatus = useSelector(checkEmailExistsModule.selectCheckEmailStatus);
    const checkEmailValue = useSelector(checkEmailExistsModule.selectCheckEmailValue);

    switch (checkEmailStatus) {
        case checkEmailExistsModule.CHECK_STATUS_START:
            return <CheckingEmail email={checkEmailValue} />;
        case checkEmailExistsModule.CHECK_STATUS_EXISTS:
            return <ExistingEmail email={checkEmailValue}
                                  onCancel={() => dispatch(checkEmailExistsModule.reset())}/>;
        case checkEmailExistsModule.CHECK_STATUS_UNKNOWN:
            return <UnknownEmail email={checkEmailValue}
                                 onCancel={() => dispatch(checkEmailExistsModule.reset())}/>;
        default:
            return <EmailEntry onSubmit={(email) => dispatch(checkEmailExistsModule.checkEmailExists(email))}/>;
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
