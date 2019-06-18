import React, {FunctionComponent} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {Formik} from "formik";
import {FormikErrorMessage} from "../../common/FormikErrorMessage";
import * as Yup from "yup";
import {StringSchema} from "yup";

/** The props that the UnknownEmail area needs */
type UnknownEmailProps = {
    email: string,
    onCancel: () => void,
};

/**
 * Component for when an Unknown Email Address was entered and we need to register a new user
 * @constructor
 */
export const UnknownEmail: FunctionComponent<UnknownEmailProps> = ({email, onCancel}) => {
    const { t } = useTranslation();

    const schema = Yup.object().shape({
        email: Yup.string()
            .required(t('loginArea.email.errors.required')),
        name: Yup.string()
            .required(t('loginArea.name.errors.required')),
        password: Yup.string()
            .required(t('loginArea.password.errors.required')),
        password2: Yup.string()
            .required(t('loginArea.password2.errors.required'))
            .when('password', (password: string, schema: StringSchema) =>
                schema.oneOf([password], t('loginArea.password2.errors.match'))),
    });

    return (
        <Formik initialValues={{email: email, name: '', password: '', password2: ''}}
                validationSchema={schema}
                onSubmit={(values) => console.log(values)}>
            {({values, isValid, errors, touched, handleSubmit, handleChange, handleBlur}) =>
                <Form onSubmit={handleSubmit} error={!isValid}>
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
                            {t('loginArea.name.label')}
                        </label>
                        <Form.Input fluid
                                    placeholder={t('loginArea.name.placeholder')}
                                    name="name"
                                    type="text"
                                    value={values.name}
                                    error={touched.name && errors.name !== undefined}
                                    autoFocus
                                    onChange={handleChange}
                                    onBlur={handleBlur} />
                        <FormikErrorMessage name="name" />
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
                    <Form.Field required>
                        <label>
                            {t('loginArea.password2.label')}
                        </label>
                        <Form.Input fluid
                                    placeholder={t('loginArea.password2.placeholder')}
                                    name="password2"
                                    type="password"
                                    value={values.password2}
                                    error={touched.password2 && errors.password2 !== undefined}
                                    onChange={handleChange}
                                    onBlur={handleBlur} />
                        <FormikErrorMessage name="password2" />
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
