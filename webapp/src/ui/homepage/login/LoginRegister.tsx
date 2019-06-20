import React, {FunctionComponent} from 'react';
import {useDispatch, useSelector} from 'react-redux'
import {Header, Segment} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {EmailEntry} from "./EmailEntry";
import checkEmailExistsModule from "../../../authentication/checkEmailExists";
import {Login} from "./Login";
import {Register} from "./Register";

/**
 * The body of the Login/Register area
 * @constructor
 */
export const LoginRegisterBody: FunctionComponent<object> = () => {
    const dispatch = useDispatch();
    const checkEmailStatus = useSelector(checkEmailExistsModule.selectCheckEmailStatus);
    const checkEmailValue = useSelector(checkEmailExistsModule.selectCheckEmailValue);

    switch (checkEmailStatus) {
        case checkEmailExistsModule.CHECK_STATUS_EXISTS:
            return <Login email={checkEmailValue}
                          onCancel={() => dispatch(checkEmailExistsModule.reset())}/>;
        case checkEmailExistsModule.CHECK_STATUS_UNKNOWN:
            return <Register email={checkEmailValue}
                             onCancel={() => dispatch(checkEmailExistsModule.reset())}/>;
        default:
            return <EmailEntry />;
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
