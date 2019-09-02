import React, {FunctionComponent} from 'react';
import {Container} from "semantic-ui-react";
import {CreateWorldBreadcrumbs} from "./CreateWorldBreadcrumbs";
import {CreateWorldHeader} from "./CreateWorldHeader";
import {useTranslation} from "react-i18next";

/**
 * The Create World Page
 * @constructor
 */
export const CreateWorldPage : FunctionComponent<any> = () => {
    const { t } = useTranslation();

    return (
        <Container>
            <CreateWorldBreadcrumbs />
            <CreateWorldHeader />
        </Container>
    );
};
