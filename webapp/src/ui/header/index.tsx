import React, {FunctionComponent} from 'react';
import {Menu} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {UserMenu} from "./UserMenu";
import {Authenticated} from "../common/Authenticated";
import {WorldsMenu} from "./WorldsMenu";

/** The props that a HeaderBar needs */
type HeaderBarProps = {};

/**
 * The main header bar for the application
 * @constructor
 */
export const HeaderBar: FunctionComponent<HeaderBarProps> = () => {
    const { t } = useTranslation();

    return (
        <Menu inverted fluid fixed="top">
            <Menu.Item header>
                {t('page.header')}
            </Menu.Item>

            <Menu.Menu position='right'>
                <Authenticated>
                    <WorldsMenu/>
                    <UserMenu />
                </Authenticated>
            </Menu.Menu>
        </Menu>
    );
};
