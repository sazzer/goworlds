import React, {FunctionComponent, PropsWithChildren} from 'react';
import {useSelector} from "react-redux";
import {selectCurrentUserId} from "../../authentication/currentUserId";

/**
 * Display the nested children only if we're authenticated - i.e. we have a Current User ID in the state
 * @constructor
 */
export const Authenticated: FunctionComponent<PropsWithChildren<any>> = ({children}) => {
    const currentUserId = useSelector(selectCurrentUserId);

    if (currentUserId) {
        return children;
    } else {
        return <></>;
    }
};

/**
 * Display the nested children only if we're not authenticated - i.e. we do not have a Current User ID in the state
 * @constructor
 */
export const Unauthenticated: FunctionComponent<PropsWithChildren<any>> = ({children}) => {
    const currentUserId = useSelector(selectCurrentUserId);

    if (currentUserId) {
        return <></>;
    } else {
        return children;
    }
};
