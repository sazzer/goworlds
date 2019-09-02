/** The props that a HeaderBar needs */
import React, {FunctionComponent} from "react";
import {useTranslation} from "react-i18next";
import {Dropdown, Icon} from "semantic-ui-react";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {selectCurrentUserId} from "../../authentication/currentUserId";
import {loadUser, selectUserById} from "../../users/users";
import {Link} from "react-router-dom";
import {logout} from "../../authentication/logout";
import {User} from "../../users/user";

type UserMenuProps = {};

/**
 * The User menu in the application
 * @constructor
 */
export const UserMenu: FunctionComponent<UserMenuProps> = () => {
    const { t } = useTranslation();
    const dispatch = useDispatch();
    const currentUserId = useSelector(selectCurrentUserId);
    const currentUser : User | undefined = useSelector(selectUserById(currentUserId), shallowEqual);

    if (currentUser === undefined) {
        dispatch(loadUser(currentUserId));
    }

    return (
        <Dropdown item text={currentUser === undefined ? '' : currentUser.name} loading={currentUser === undefined} data-test="UserMenu">
            <Dropdown.Menu>
                <Link to="/profile" className="ui item">
                    <Icon name="edit" />
                    {t('userMenu.editProfile')}
                </Link>
                <Dropdown.Divider />
                <div className="ui item" onClick={() => dispatch(logout())}>
                    <Icon name="log out" />
                    {t('userMenu.logOut')}
                </div>
            </Dropdown.Menu>
        </Dropdown>
    );
};
