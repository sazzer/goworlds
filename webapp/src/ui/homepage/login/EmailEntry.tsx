import React, {FunctionComponent} from 'react';
import {Button, Form} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {Formik} from "formik";
import * as Yup from 'yup';
import {FormikErrorMessage} from "../../common/FormikErrorMessage";
import {useDispatch} from "react-redux";
import checkEmailExistsModule from "../../../authentication/checkEmailExists";

/**
 * The EmailEntry area
 * @constructor
 */
export const EmailEntry: FunctionComponent<object> = () => {
    const { t } = useTranslation();
    const dispatch = useDispatch();

    const schema = Yup.object().shape({
        email: Yup.string()
            .required(t('loginArea.email.errors.required'))
            .email(t('loginArea.email.errors.email'))
    });

    const doSubmit = (email: string) => {
        dispatch(checkEmailExistsModule.checkEmailExists(email));
    };

    return (
        <Formik initialValues={{email: ''}}
                validationSchema={schema}
                onSubmit={(values) => doSubmit(values.email)}>
            {({values, isValid, errors, handleSubmit, handleChange, handleBlur}) =>
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
