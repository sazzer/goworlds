/** The props that a HeaderBar needs */
import React, {FunctionComponent} from "react";
import {useTranslation} from "react-i18next";
import {Dropdown, Menu} from "semantic-ui-react";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {selectCurrentUserId} from "../../authentication/currentUserId";
import {selectUserById, User, loadUser} from "../../users/users";

type UserMenuProps = {};

/**
 * The main header bar for the application
 * @constructor
 */
export const UserMenu: FunctionComponent<UserMenuProps> = () => {
    const { t } = useTranslation();
    const dispatch = useDispatch();
    const currentUserId = useSelector(selectCurrentUserId);
    const currentUser : User | undefined = useSelector(selectUserById(currentUserId), shallowEqual);

    if (currentUserId) {
        if (currentUser === undefined) {
            dispatch(loadUser(currentUserId));
        }

        return (
            <Menu.Menu position="right">
                <Dropdown item text={currentUser === undefined ? '' : currentUser.name} loading={currentUser === undefined}>
                    <Dropdown.Menu>
                        <Dropdown.Item icon='edit' text={t('userMenu.editProfile')} />
                        <Dropdown.Divider />
                        <Dropdown.Item icon='log out' text={t('userMenu.logOut')} />
                    </Dropdown.Menu>
                </Dropdown>

            </Menu.Menu>
        );
    }

    return <></>;
};
