import React, {FunctionComponent} from 'react';
import {Container, Grid, Loader, Menu} from "semantic-ui-react";
import {ProfileBreadcrumbs} from "./ProfileBreadcrumbs";
import {ProfileHeader} from "./ProfileHeader";
import {NavLink, Route} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {shallowEqual, useSelector} from "react-redux";
import {selectCurrentUserId} from "../../authentication/currentUserId";
import {selectUserById} from "../../users/users";
import {ProfileForm} from "./ProfileForm";
import {PasswordForm} from "./PasswordForm";
import {User} from "../../users/user";

/**
 * The Profile Page
 * @constructor
 */
export const ProfilePage : FunctionComponent<any> = () => {
    const { t } = useTranslation();

    const currentUserId = useSelector(selectCurrentUserId);
    const currentUser : User | undefined = useSelector(selectUserById(currentUserId), shallowEqual);

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
                    { currentUser !== undefined && <Route path='/profile' exact render={() => <ProfileForm userId={currentUserId} />} />}
                    { currentUser !== undefined && <Route path='/profile/password' exact render={() => <PasswordForm user={currentUser} />} />}
                </Grid.Column>
            </Grid>
        </Container>
    );
};
