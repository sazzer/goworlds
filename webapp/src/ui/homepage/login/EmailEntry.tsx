import React, {FunctionComponent, useState} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {Formik} from "formik";
import * as Yup from 'yup';
import {FormikErrorMessage} from "../../common/FormikErrorMessage";
import {useDispatch} from "react-redux";
import checkEmailExistsModule from "../../../authentication/checkEmailExists";
import {ErrorMessage} from "../../common/ErrorMessage";

/** The props that the EmailEntry area needs */
type EmailEntryProps = {
    onSubmit: (email: string, status: boolean) => void,
};

/**
 * Component for entering an Email Address to either Login as or Register
 * @constructor
 */
export const EmailEntry: FunctionComponent<EmailEntryProps> = ({onSubmit}) => {
    const { t } = useTranslation();
    const dispatch = useDispatch();
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | undefined>(undefined);

    const schema = Yup.object().shape({
        email: Yup.string()
            .required(t('loginArea.email.errors.required'))
            .email(t('loginArea.email.errors.email'))
    });

    const doSubmit = (email: string) => {
        setSubmitting(true);
        dispatch(checkEmailExistsModule.checkEmailExists(email, (status, err) => {
            setSubmitting(false);
            if (err) {
                setError('unexpected_error');
            } else {
                onSubmit(email, status);
            }
        }));
    };

    return (
        <Formik initialValues={{email: ''}}
                validationSchema={schema}
                validateOnChange={true}
                validateOnBlur={false}
                onSubmit={(values) => doSubmit(values.email)}>
            {({values, isValid, errors, handleSubmit, handleChange, handleBlur}) =>
                <Form onSubmit={handleSubmit}
                      error={!isValid || error !== undefined}
                      loading={submitting}
                      data-test="EmailEntry">
                    <Form.Field required data-test="email">
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
                    <ErrorMessage errors={[
                        error && t('loginArea.submit.errors.' + error)
                    ]}/>
                </Form>
            }
        </Formik>
    );
};
