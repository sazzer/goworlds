import React, {FunctionComponent} from 'react';
import {Header} from "semantic-ui-react";
import {NamedUser} from "./NamedUser";

/** The type for the header */
export declare type ProfileHeaderProps = {
    user: NamedUser
};

/**
 * Header to render on the Profile page
 * @constructor
 */
export const ProfileHeader: FunctionComponent<ProfileHeaderProps> = ({user}) => {
    return <Header size='large' dividing>
        {user.name}
    </Header>;
}
