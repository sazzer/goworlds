import {Then, When} from "cucumber";
import * as chai from 'chai';
import {getPageModel} from "../../browser";
import {basePage} from "../../pages/BasePage";
import {userProfilePage} from "../../pages/userProfile/UserProfilePage";

When('I open the Change Password page', async () => {
    const basePageModel = await getPageModel(basePage);
    const userMenu = await basePageModel.getUserMenu();

    await userMenu.openUserProfile();

    const userProfilePageModel = await getPageModel(userProfilePage);
    await userProfilePageModel.getChangePasswordForm();
});