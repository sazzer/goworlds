import React, {FunctionComponent} from 'react';
import {Message} from "semantic-ui-react";

/** The props that a ErrorMessage needs */
type ErrorMessageProps = {
    errors: any[],
    testName?: string,
};

/**
 * A section displaying error messages
 * @constructor
 */
export const ErrorMessage: FunctionComponent<ErrorMessageProps> = ({errors, testName}) => {
    if (errors.length > 1) {
        return (
            <Message error list={errors} data-test={testName} />
        );
    } else if (errors.length === 1) {
        return (
            <Message error data-test={testName}>
                {errors[0]}
            </Message>
        );
    } else {
        return <></>;
    }
};
