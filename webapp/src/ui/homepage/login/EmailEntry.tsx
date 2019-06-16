import React, {FunctionComponent} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {Formik} from "formik";
import * as Yup from 'yup';
import {FormikErrorMessage} from "../../common/FormikErrorMessage";

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

    const schema = Yup.object().shape({
        email: Yup.string().required(t('loginArea.email.errors.required'))
    });

    return (
        <Formik initialValues={{email: ''}}
                validationSchema={schema}
                onSubmit={(values) => onSubmit(values.email)}>
            {({values, errors, handleSubmit, handleChange, handleBlur}) =>
                <Form onSubmit={handleSubmit} error={errors.email !== undefined}>
                    <Form.Field>
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
