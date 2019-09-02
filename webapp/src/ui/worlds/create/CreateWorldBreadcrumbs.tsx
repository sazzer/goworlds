import React, {FunctionComponent} from 'react';
import {Breadcrumb} from "semantic-ui-react";
import {Link} from "react-router-dom";
import {useTranslation} from "react-i18next";

/** The type for the breadcrumbs */
export declare type CreateWorldBreadcrumbsProps = {
};

/**
 * Breadcrumbs to render on the Create World page
 * @constructor
 */
export const CreateWorldBreadcrumbs: FunctionComponent<CreateWorldBreadcrumbsProps> = () => {
    const { t } = useTranslation();

    return (
        <Breadcrumb>
            <Breadcrumb.Section>
                <Link to="/">{t('worlds.create.breadcrumbs.home')}</Link>
            </Breadcrumb.Section>
            <Breadcrumb.Divider />
            <Breadcrumb.Section active>{t('worlds.create.breadcrumbs.label')}</Breadcrumb.Section>
        </Breadcrumb>
    );
}
