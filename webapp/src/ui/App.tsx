import React from 'react';
import {Container} from "semantic-ui-react";
import { useTranslation } from 'react-i18next';

/**
 * The main UI structure
 * @constructor
 */
export default function App () {
    const { t } = useTranslation();

    return (
        <Container>
            {t('hello')}
        </Container>
    );
}
