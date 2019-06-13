import React, {FunctionComponent, useState} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";

/** The props that the ExistingEmail area needs */
type ExistingEmailProps = {
    email: string,
    onCancel: () => void,
};

/**
 * The ExistingEmail area
 * @constructor
 */
export const ExistingEmail: FunctionComponent<ExistingEmailProps> = ({email, onCancel}) => {
    const { t } = useTranslation();
    const [password, setPassword] = useState("");

    return (
        <Form>
            <Form.Field>
                <label>
                    {t('loginArea.email.label')}
                </label>
                <input placeholder={t('loginArea.email.placeholder')}
                       name="email"
                       type="text"
                       value={email}
                       readOnly />
                <Form.Field>
                    <label>
                        {t('loginArea.password.label')}
                    </label>
                    <input placeholder={t('loginArea.password.placeholder')}
                           name="password"
                           type="password"
                           value={password}
                           onChange={e => setPassword(e.target.value)}
                           autoFocus/>
                </Form.Field>
                <Button type="submit" primary>
                    {t('loginArea.submit.login')}
                </Button>
                <Button type="reset" negative onClick={onCancel}>
                    {t('loginArea.submit.cancel')}
                </Button>
            </Form.Field>
        </Form>
    );
};
