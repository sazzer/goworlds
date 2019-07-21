import React, {FunctionComponent, useEffect, useState} from 'react';
import {Button, Form, Message} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import * as Yup from "yup";
import {Formik} from "formik";
import {FormikErrorMessage} from "../common/FormikErrorMessage";
import {ErrorMessage} from "../common/ErrorMessage";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {updateUserProfile} from "../../users/updateProfile";
import {ProblemError} from "../../api";
import {User} from "../../users/user";
import {loadUser, selectUserById} from "../../users/users";

/** The props that the InnerProfileForm area needs */
type InnerProfileFormProps = {
    user: User,
    loading: boolean,
};

/**
 * Component for editing the core profile details for a user
 * @constructor
 */
const InnerProfileForm: FunctionComponent<InnerProfileFormProps> = ({user, loading}) => {
    const { t, i18n } = useTranslation();
    const dispatch = useDispatch();

    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | undefined>(undefined);
    const [success, setSuccess] = useState<boolean>(false);

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
        setSuccess(false);

        dispatch(updateUserProfile(user.id, email, name, (err) => {
            setSubmitting(false);
            if (err) {
                if (err instanceof ProblemError) {
                    if (i18n.exists('profile.form.submit.errors.' + err.type)) {
                        setError(err.type);
                    } else {
                        setError('unexpected_error');
                    }
                } else {
                    setError('unexpected_error');
                }
            } else {
                setSuccess(true);
            }
        }));
    };

    return (
        <Formik initialValues={user}
                validationSchema={schema}
                onSubmit={(values) => doSubmit(values.email, values.name)}>
            {({values, isValid, errors, touched, handleSubmit, handleChange, handleBlur, handleReset, dirty}) =>
                <Form onSubmit={handleSubmit}
                      error={!isValid || error !== undefined}
                      loading={submitting || loading}
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
                        {t('profile.form.submit.save')}
                    </Button>
                    <Button type="reset" negative onClick={handleReset} disabled={!dirty}>
                        {t('profile.form.submit.reset')}
                    </Button>
                    <ErrorMessage testName="ProfileFormErrors" errors={[
                        error && t('profile.form.submit.errors.' + error)
                    ]}/>
                    { success && <Message positive>{t('profile.form.submit.success')}</Message> }
                </Form>
            }
        </Formik>
    );
};

/** The props that the ProfileForm area needs */
type ProfileFormProps = {
    userId: string,
};

/**
 * Wrapper around the Profile Form to request the user details are loaded first
 * @constructor
 */
export const ProfileForm: FunctionComponent<ProfileFormProps> = ({userId}) => {
    const dispatch = useDispatch();
    const [loading, setLoading] = useState(false);

    const user : User | undefined = useSelector(selectUserById(userId), shallowEqual);

    useEffect(() => {
        setLoading(true);
        dispatch(loadUser(userId, true, () => {
            setLoading(false);
        }));
    }, [userId, dispatch]);

    if (user === undefined) {
        return <></>;
    }

    return <InnerProfileForm user={user} loading={loading} />;
};
