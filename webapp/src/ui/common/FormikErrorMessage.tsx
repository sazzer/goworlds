import React, {FunctionComponent} from 'react';
import {ErrorMessage} from "./ErrorMessage";
import {ErrorMessage as FormikErrorMessageWrapper} from "formik";

/** The props that a FormikErrorMessage needs */
type FormikErrorMessageProps = {
    name: string,
};

/**
 * A section displaying error messages for a single Formik field
 * @constructor
 */
export const FormikErrorMessage: FunctionComponent<FormikErrorMessageProps> = ({name}) =>
    (
        <FormikErrorMessageWrapper name={name}>
            {msg => <ErrorMessage errors={[msg]} /> }
        </FormikErrorMessageWrapper>
    );
