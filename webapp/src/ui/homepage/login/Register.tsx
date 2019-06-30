import React, {FunctionComponent, useState} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {Formik} from "formik";
import {FormikErrorMessage} from "../../common/FormikErrorMessage";
import * as Yup from "yup";
import {StringSchema} from "yup";
import {useDispatch} from "react-redux";
import {register} from "../../../authentication/register";
import {ErrorMessage} from "../../common/ErrorMessage";
import {ProblemError} from "../../../api";

/** The props that the Register area needs */
type RegisterProps = {
    email: string,
    onCancel: () => void,
};

/**
 * Component for registering a new account
 * @constructor
 */
export const Register: FunctionComponent<RegisterProps> = ({email, onCancel}) => {
    const { t, i18n } = useTranslation();
    const dispatch = useDispatch();
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | undefined>(undefined);

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

    const doSubmit = (email: string, name: string, password: string) => {
        setSubmitting(true);
        setError(undefined);

        dispatch(register(email, name, password, (err) => {
            setSubmitting(false);
            if (err) {
                if (err instanceof ProblemError) {
                    if (i18n.exists('loginArea.submit.errors.' + err.type)) {
                        setError(err.type);
                    } else {
                        setError('unexpected_error');
                    }
                } else {
                    setError('unexpected_error');
                }
            }
        }));
    };

    return (
        <Formik initialValues={{email: email, name: '', password: '', password2: ''}}
                validationSchema={schema}
                validateOnBlur={false}
                validateOnChange={true}
                onSubmit={(values) => doSubmit(values.email, values.name, values.password)}>
            {({values, isValid, errors, touched, handleSubmit, handleChange, handleBlur}) =>
                <Form onSubmit={handleSubmit}
                      error={!isValid || error !== undefined}
                      loading={submitting}
                      data-test="Register">
                    <Form.Field required data-test="email">
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
                    <Form.Field required data-test="name">
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
                    <Form.Field required data-test="password">
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
                    <Form.Field required data-test="password2">
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
                    <ErrorMessage testName="RegisterErrors" errors={[
                        error && t('loginArea.submit.errors.' + error)
                    ]}/>
                </Form>
            }
        </Formik>
    );
};
