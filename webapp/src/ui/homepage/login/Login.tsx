import React, {FunctionComponent, useState} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import * as Yup from "yup";
import {Formik} from "formik";
import {FormikErrorMessage} from "../../common/FormikErrorMessage";

/** The props that the Login area needs */
type LoginProps = {
    email: string,
    onCancel: () => void,
};

/**
 * Component for logging in to an existing account
 * @constructor
 */
export const Login: FunctionComponent<LoginProps> = ({email, onCancel}) => {
    const { t } = useTranslation();
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | undefined>(undefined);

    const schema = Yup.object().shape({
        email: Yup.string()
            .required(t('loginArea.email.errors.required')),
        password: Yup.string()
            .required(t('loginArea.password.errors.required')),
    });

    const doSubmit = (email: string, password: string) => {
        setSubmitting(true);
    };

    return (
        <Formik initialValues={{email: email, password: ''}}
                validationSchema={schema}
                onSubmit={(values) => doSubmit(values.email, values.password)}>
            {({values, isValid, errors, touched, handleSubmit, handleChange, handleBlur}) =>
                <Form onSubmit={handleSubmit} error={!isValid || error !== undefined} loading={submitting}>
                    <Form.Field required>
                        <label>
                            {t('loginArea.email.label')}
                        </label>
                        <Form.Input fluid
                                    placeholder={t('loginArea.email.placeholder')}
                                    name="email"
                                    type="text"
                                    value={values.email}
                                    readOnly />
                    </Form.Field>
                    <Form.Field required>
                        <label>
                            {t('loginArea.password.label')}
                        </label>
                        <Form.Input fluid
                                    placeholder={t('loginArea.password.placeholder')}
                                    name="password"
                                    type="password"
                                    value={values.password}
                                    error={touched.password && errors.password !== undefined}
                                    onChange={handleChange}
                                    onBlur={handleBlur} />
                        <FormikErrorMessage name="password" />
                    </Form.Field>
                    <Button type="submit" primary>
                        {t('loginArea.submit.register')}
                    </Button>
                    <Button type="reset" negative onClick={onCancel}>
                        {t('loginArea.submit.cancel')}
                    </Button>
                </Form>
            }
        </Formik>
    );
};
