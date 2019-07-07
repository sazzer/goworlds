import React, {FunctionComponent} from 'react';
import {Breadcrumb} from "semantic-ui-react";
import {Link} from "react-router-dom";
import {Trans, useTranslation} from "react-i18next";
import {NamedUser} from "./NamedUser";

/** The type for the breadcrumbs */
export declare type ProfileBreadcrumbsProps = {
    user: NamedUser
};

/**
 * Breadcrumbs to render on the Profile page
 * @constructor
 */
export const ProfileBreadcrumbs: FunctionComponent<ProfileBreadcrumbsProps> = ({user}) => {
    const { t } = useTranslation();

    return (
        <Breadcrumb>
            <Breadcrumb.Section>
                <Link to="/">{t('profile.breadcrumbs.home')}</Link>
            </Breadcrumb.Section>
            <Breadcrumb.Divider />
            <Breadcrumb.Section active>
                <Trans i18nKey='profile.breadcrumbs.profile' values={user}>
                    Profile: <Link to="/profile">{user.name}</Link>
                </Trans>
            </Breadcrumb.Section>
        </Breadcrumb>
    );
}
