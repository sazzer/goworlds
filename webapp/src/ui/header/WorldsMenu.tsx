import React, {FunctionComponent} from "react";
import {Dropdown, Icon} from "semantic-ui-react";
import {useTranslation} from "react-i18next";
import {Link} from "react-router-dom";

/** The props that a Worlds Bar needs */
type WorldsMenuProps = {};

/**
 * The Worlds menu in the application
 * @constructor
 */
export const WorldsMenu: FunctionComponent<WorldsMenuProps> = () => {
    const { t } = useTranslation();

    return (
        <Dropdown item text={t('worldsMenu.label')} data-test="WorldsMenu">
            <Dropdown.Menu>
                <Dropdown.Divider />
                <Link to="/worlds/create" className="ui item">
                    <Icon name="write" />
                    {t('worldsMenu.create')}
                </Link>
            </Dropdown.Menu>
        </Dropdown>
    );
};
