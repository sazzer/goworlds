import React, {FunctionComponent} from 'react';
import {Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";

/** The props that the CheckingEmail area needs */
type CheckingEmailProps = {
    email: string,
};

/**
 * The CheckingEmail area
 * @constructor
 */
export const CheckingEmail: FunctionComponent<CheckingEmailProps> = ({email}) => {
    const { t } = useTranslation();

    return (
        <Form loading>
            <Form.Field>
                <label>
                    {t('loginArea.email.label')}
                </label>
                <input placeholder={t('loginArea.email.placeholder')}
                       name="email"
                       type="text"
                       value={email}/>
            </Form.Field>
        </Form>
    );
};
