import React, {FunctionComponent} from 'react';
import {Container, Grid, Loader, Menu} from "semantic-ui-react";
import {ProfileBreadcrumbs} from "./ProfileBreadcrumbs";
import {ProfileHeader} from "./ProfileHeader";
import {NavLink} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {selectCurrentUserId} from "../../authentication/currentUserId";
import {loadUser, selectUserById, User} from "../../users/users";

/**
 * The Profile Page
 * @constructor
 */
export const ProfilePage : FunctionComponent<any> = () => {
    const { t } = useTranslation();

    const dispatch = useDispatch();
    const currentUserId = useSelector(selectCurrentUserId);
    const currentUser : User | undefined = useSelector(selectUserById(currentUserId), shallowEqual);

    dispatch(loadUser(currentUserId, true));

    const namedUser = currentUser || {
        name: t('profile.loading')
    };

    return (
        <Container>
            <ProfileBreadcrumbs user={namedUser} />
            <ProfileHeader user={namedUser} />

            <Grid>
                <Grid.Column width={4}>
                    <Menu fluid vertical tablular pointing>
                        <NavLink to='/profile' exact className="item">{t('profile.menu.profile')}</NavLink>
                        <NavLink to='/profile/password' exact className="item">{t('profile.menu.password')}</NavLink>
                    </Menu>
                </Grid.Column>
                <Grid.Column stretched width={12}>
                    { currentUser === undefined && <Loader active /> }
                    { currentUser !== undefined && "Profile Page Here" }
                </Grid.Column>
            </Grid>
        </Container>
    );
};