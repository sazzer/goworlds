import React, {FunctionComponent} from 'react';
import {Container} from "semantic-ui-react";
import {CreateWorldBreadcrumbs} from "./CreateWorldBreadcrumbs";
import {CreateWorldHeader} from "./CreateWorldHeader";

/**
 * The Create World Page
 * @constructor
 */
export const CreateWorldPage : FunctionComponent<any> = () => {
    return (
        <Container>
            <CreateWorldBreadcrumbs />
            <CreateWorldHeader />
        </Container>
    );
};
