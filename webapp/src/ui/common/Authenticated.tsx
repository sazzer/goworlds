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

/**
 * Shape of the props that the IsAuthenticated component takes
 */
export declare type IsAuthenticatedProps = {
    render: (auth: boolean) => any | null,
};

/**
 * Component to allow us to conditionally render based on whether we're authenticated or not
 * @param render Render Prop for rendering the child component
 * @constructor
 */
export const IsAuthenticated: FunctionComponent<IsAuthenticatedProps> = ({render}) => {
    const currentUserId = useSelector(selectCurrentUserId);

    return render(currentUserId !== undefined);
};
