import React, {FunctionComponent, useState} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";

/** The props that the UnknownEmail area needs */
type UnknownEmailProps = {
    email: string,
    onCancel: () => void,
};

/**
 * The UnknownEmail area
 * @constructor
 */
export const UnknownEmail: FunctionComponent<UnknownEmailProps> = ({email, onCancel}) => {
    const { t } = useTranslation();
    const [name, setName] = useState("");
    const [password, setPassword] = useState("");
    const [password2, setPassword2] = useState("");

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
                        {t('loginArea.name.label')}
                    </label>
                    <input placeholder={t('loginArea.name.placeholder')}
                           name="name"
                           type="text"
                           value={name}
                           onChange={e => setName(e.target.value)}
                           autoFocus/>
                </Form.Field>
                <Form.Field>
                    <label>
                        {t('loginArea.password.label')}
                    </label>
                    <input placeholder={t('loginArea.password.placeholder')}
                           name="password"
                           type="password"
                           value={password}
                           onChange={e => setPassword(e.target.value)} />
                </Form.Field>
                <Form.Field>
                    <label>
                        {t('loginArea.password2.label')}
                    </label>
                    <input placeholder={t('loginArea.password2.placeholder')}
                           name="password2"
                           type="password"
                           value={password2}
                           onChange={e => setPassword2(e.target.value)} />
                </Form.Field>
                <Button type="submit" primary>
                    {t('loginArea.submit.register')}
                </Button>
                <Button type="reset" negative onClick={onCancel}>
                    {t('loginArea.submit.cancel')}
                </Button>
            </Form.Field>
        </Form>
    );
};
