import React, {FunctionComponent} from 'react';
import {Message} from "semantic-ui-react";

/** The props that a ErrorMessage needs */
type ErrorMessageProps = {
    errors: any[],
};

/**
 * A section displaying error messages
 * @constructor
 */
export const ErrorMessage: FunctionComponent<ErrorMessageProps> = ({errors}) => {
    if (errors.length > 1) {
        return (
            <Message error list={errors} />
        );
    } else if (errors.length === 1) {
        return (
            <Message error>
                {errors[0]}
            </Message>
        );
    } else {
        return <></>;
    }
};
