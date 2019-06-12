import React, {FunctionComponent, useState} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";

/** The props that the EmailEntry area needs */
type EmailEntryProps = {
    onSubmit: (email: string) => void
};

/**
 * The EmailEntry area
 * @constructor
 */
export const EmailEntry: FunctionComponent<EmailEntryProps> = ({onSubmit}) => {
    const { t } = useTranslation();
    const [email, setEmail] = useState("");

    const doSubmit = () => {
        if (email) {
            onSubmit(email);
        }
    };

    return (
        <Form onSubmit={doSubmit}>
            <Form.Field>
                <label>
                    {t('loginArea.email.label')}
                </label>
                <input placeholder={t('loginArea.email.placeholder')}
                       name="email"
                       type="text"
                       value={email}
                       onChange={e => setEmail(e.target.value)}
                       autoFocus/>
            </Form.Field>
            <Button type="submit" primary>
                {t('loginArea.submit.loginRegister')}
            </Button>
        </Form>
    );
};
