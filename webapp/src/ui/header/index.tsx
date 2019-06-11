import React from 'react';
import {Menu} from "semantic-ui-react";
import {useTranslation} from "react-i18next";

/**
 * The main header bar for the application
 * @constructor
 */
export default function HeaderBar() {
    const { t } = useTranslation();

    return (
        <Menu inverted fluid fixed="top">
            <Menu.Item header>
                {t('page.header')}
            </Menu.Item>
        </Menu>
    )
}
