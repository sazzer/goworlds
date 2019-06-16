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
    const [emailAbsent, setEmailAbsent] = useState(false);

    const doSubmit = () => {
        if (email) {
            setEmailAbsent(false);
            onSubmit(email);
        } else {
            setEmailAbsent(true);
        }
    };

    const doChangeEmail = (email: string) => {
        setEmail(email);
        setEmailAbsent(false);
    };

    return (
        <Form onSubmit={doSubmit} error={emailAbsent}>
            <Form.Field>
                <label>
                    {t('loginArea.email.label')}
                </label>
                <Form.Input fluid
                            placeholder={t('loginArea.email.placeholder')}
                            name="email"
                            type="text"
                            value={email}
                            error={emailAbsent}
                            onChange={e => doChangeEmail(e.target.value)}
                            autoFocus />
            </Form.Field>
            <Button type="submit" primary>
                {t('loginArea.submit.loginRegister')}
            </Button>
        </Form>
    );
};
