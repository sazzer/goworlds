import React, {FunctionComponent, useState} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {Formik} from "formik";
import * as Yup from 'yup';
import {FormikErrorMessage} from "../../common/FormikErrorMessage";
import {checkEmail} from "../../../authentication/checkEmail";

/** The props that the EmailEntry area needs */
type EmailEntryProps = {
    onSubmit: (email: string, known: boolean) => void
};

/**
 * Component for allowing an email to be entered to start authentication
 * @constructor
 */
export const EmailEntry: FunctionComponent<EmailEntryProps> = ({onSubmit}) => {
    const { t } = useTranslation();
    const [submitting, setSubmitting] = useState<boolean>();

    const schema = Yup.object().shape({
        email: Yup.string()
            .required(t('loginArea.email.errors.required'))
            .email(t('loginArea.email.errors.email'))
    });

    const doSubmit = (email: string) => {
        setSubmitting(true);
        checkEmail(email).subscribe((status: boolean) => {
            setSubmitting(false);
            onSubmit(email, status);
        });
    };

    return (
        <Formik initialValues={{email: ''}}
                validationSchema={schema}
                onSubmit={(values) => doSubmit(values.email)}>
            {({values, isValid, errors, handleSubmit, handleChange, handleBlur}) =>
                <Form onSubmit={handleSubmit} error={!isValid} loading={submitting}>
                    <Form.Field required>
                        <label>
                            {t('loginArea.email.label')}
                        </label>
                        <Form.Input fluid
                                    placeholder={t('loginArea.email.placeholder')}
                                    name="email"
                                    type="text"
                                    value={values.email}
                                    error={errors.email !== undefined}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    autoFocus />
                        <FormikErrorMessage name="email" />
                    </Form.Field>
                    <Button type="submit" primary>
                        {t('loginArea.submit.loginRegister')}
                    </Button>
                </Form>
            }
        </Formik>
    );
};
