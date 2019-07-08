import React, {FunctionComponent, useState} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import * as Yup from "yup";
import {Formik} from "formik";
import {FormikErrorMessage} from "../common/FormikErrorMessage";
import {ErrorMessage} from "../common/ErrorMessage";
import {User} from "../../users/users";

/** The props that the ProfileForm area needs */
type ProfileFormProps = {
    user: User,
};

/**
 * Component for editing the core profile details for a user
 * @constructor
 */
export const ProfileForm: FunctionComponent<ProfileFormProps> = ({user}) => {
    const { t } = useTranslation();
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | undefined>(undefined);

    const schema = Yup.object().shape({
        email: Yup.string()
            .required(t('profile.form.email.errors.required'))
            .email(t('profile.form.email.errors.email')),
        name: Yup.string()
            .required(t('profile.form.name.errors.required')),
    });

    const doSubmit = (email: string, name: string) => {
        setSubmitting(true);
        setError(undefined);
    };

    return (
        <Formik initialValues={user}
                validationSchema={schema}
                onSubmit={(values) => doSubmit(values.email, values.name)}>
            {({values, isValid, errors, touched, handleSubmit, handleChange, handleBlur, handleReset, dirty}) =>
                <Form onSubmit={handleSubmit}
                      error={!isValid || error !== undefined}
                      loading={submitting}
                      data-test="ProfileForm">
                    <Form.Field required data-test="email">
                        <label>
                            {t('profile.form.email.label')}
                        </label>
                        <Form.Input fluid
                                    placeholder={t('profile.form.email.placeholder')}
                                    name="email"
                                    type="text"
                                    value={values.email}
                                    error={touched.email && errors.email !== undefined}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    autoFocus/>
                        <FormikErrorMessage name="email" />
                    </Form.Field>
                    <Form.Field required data-test="name">
                        <label>
                            {t('profile.form.name.label')}
                        </label>
                        <Form.Input fluid
                                    placeholder={t('profile.form.name.placeholder')}
                                    name="name"
                                    type="text"
                                    value={values.name}
                                    error={touched.name && errors.name !== undefined}
                                    onChange={handleChange}
                                    onBlur={handleBlur}/>
                        <FormikErrorMessage name="name" />
                    </Form.Field>
                    <Button type="submit" primary disabled={!dirty}>
                        {t('profile.form.submit')}
                    </Button>
                    <Button type="reset" negative onClick={handleReset} disabled={!dirty}>
                        {t('profile.form.reset')}
                    </Button>
                    <ErrorMessage testName="ProfileFormErrors" errors={[
                        error && t('profile.form.submit.errors.' + error)
                    ]}/>
                </Form>
            }
        </Formik>
    );
};
