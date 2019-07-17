import React, {FunctionComponent, useState} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import * as Yup from "yup";
import {Formik} from "formik";
import {FormikErrorMessage} from "../common/FormikErrorMessage";
import {ErrorMessage} from "../common/ErrorMessage";
import {User} from "../../users/users";
import {StringSchema} from "yup";

/** The props that the PasswordForm area needs */
type PasswordFormProps = {
    user: User,
};

/**
 * Component for changing the password for a user
 * @constructor
 */
export const PasswordForm: FunctionComponent<PasswordFormProps> = ({user}) => {
    const { t } = useTranslation();
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | undefined>(undefined);

    const schema = Yup.object().shape({
        password: Yup.string()
            .required(t('profile.password.password.errors.required')),
        password2: Yup.string()
            .required(t('profile.password.password2.errors.required'))
            .when('password', (password: string, schema: StringSchema) =>
                schema.oneOf([password], t('profile.password.password2.errors.match'))),
    });

    const doSubmit = (password: string) => {
        setSubmitting(true);
        setError(undefined);
    };

    return (
        <Formik initialValues={{password: '', password2: ''}}
                validationSchema={schema}
                onSubmit={(values) => doSubmit(values.password)}>
            {({values, isValid, errors, touched, handleSubmit, handleChange, handleBlur, handleReset, dirty}) =>
                <Form onSubmit={handleSubmit}
                      error={!isValid || error !== undefined}
                      loading={submitting}
                      data-test="PasswordForm">
                    <Form.Field required data-test="password">
                        <label>
                            {t('profile.password.password.label')}
                        </label>
                        <Form.Input fluid
                                    placeholder={t('profile.password.password.placeholder')}
                                    name="password"
                                    type="password"
                                    value={values.password}
                                    error={touched.password && errors.password !== undefined}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    autoFocus />
                        <FormikErrorMessage name="password" />
                    </Form.Field>
                    <Form.Field required data-test="password2">
                        <label>
                            {t('profile.password.password2.label')}
                        </label>
                        <Form.Input fluid
                                    placeholder={t('profile.password.password2.placeholder')}
                                    name="password2"
                                    type="password"
                                    value={values.password2}
                                    error={touched.password2 && errors.password2 !== undefined}
                                    onChange={handleChange}
                                    onBlur={handleBlur} />
                        <FormikErrorMessage name="password2" />
                    </Form.Field>
                    <Button type="submit" primary disabled={!dirty}>
                        {t('profile.password.submit.save')}
                    </Button>
                    <ErrorMessage testName="PasswordFormErrors" errors={[
                        error && t('profile.password.submit.errors.' + error)
                    ]}/>
                </Form>
            }
        </Formik>
    );
};
