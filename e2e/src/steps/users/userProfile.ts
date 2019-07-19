import {TableDefinition, Then, When} from "cucumber";
import {getPageModel} from "../../browser";
import {userProfilePage} from "../../pages/userProfile/UserProfilePage";
import * as chai from "chai";

Then('The User Profile Form has details:', async (dataTable: TableDefinition) => {
    const userProfilePageModel = await getPageModel(userProfilePage);
    let userProfileForm = await userProfilePageModel.getUserProfileForm();

    await userProfileForm.assertFormData(dataTable.rowsHash());
});

When('I update the User Profile form to:', async (dataTable: TableDefinition) => {
    const userProfilePageModel = await getPageModel(userProfilePage);
    let userProfileForm = await userProfilePageModel.getUserProfileForm();

    await userProfileForm.populateForm(dataTable.rowsHash());
    await userProfileForm.submitForm();
});

Then('The User Profile Form has errors:', async (dataTable: TableDefinition) => {
    const userProfilePageModel = await getPageModel(userProfilePage);
    let userProfileForm = await userProfilePageModel.getUserProfileForm();

    await userProfileForm.assertFormErrors(dataTable.rowsHash());
});

Then('I get an error updating the user profile of {string}', async (error: string) => {
    const userProfilePageModel = await getPageModel(userProfilePage);
    let userProfileForm = await userProfilePageModel.getUserProfileForm();

    chai.expect(await userProfileForm.getError()).eq(error);
});
