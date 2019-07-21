import {TableDefinition, Then, When} from "cucumber";
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

When('I change the password to:', async (dataTable: TableDefinition) => {
    const userProfilePageModel = await getPageModel(userProfilePage);
    let changePasswordForm = await userProfilePageModel.getChangePasswordForm();

    await changePasswordForm.populateForm(dataTable.rowsHash());
    await changePasswordForm.submitForm();
});

Then('The password is updated successfully', async () => {
    const userProfilePageModel = await getPageModel(userProfilePage);
    let changePasswordForm = await userProfilePageModel.getChangePasswordForm();

    chai.expect(await changePasswordForm.hasSuccessMessage()).eq(true);
});

Then('The Change Password Form has errors:', async (dataTable: TableDefinition) => {
    const userProfilePageModel = await getPageModel(userProfilePage);
    let changePasswordForm = await userProfilePageModel.getChangePasswordForm();

    await changePasswordForm.assertFormErrors(dataTable.rowsHash());
});