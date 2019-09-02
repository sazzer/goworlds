import React, {FunctionComponent} from 'react';
import {Header} from "semantic-ui-react";
import {useTranslation} from "react-i18next";

/** The type for the header */
export declare type CreateWorldHeaderProps = {
};

/**
 * Header to render on the CreateWorld page
 * @constructor
 */
export const CreateWorldHeader: FunctionComponent<CreateWorldHeaderProps> = () => {
    const { t } = useTranslation();

    return <Header size='large' dividing>
        {t('worlds.create.title')}
    </Header>;
}
