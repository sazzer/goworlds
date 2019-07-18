import {TableDefinition, Then, When} from "cucumber";
import * as chai from 'chai';

import {getPageModel} from "../../browser";
import {homePage} from "../../pages/HomePage";

Then('the User Registration form is displayed', async () => {
    const homePageModel = await getPageModel(homePage);
    const registrationModel = await homePageModel.getUserRegistrationModel();

    chai.expect(await registrationModel.isVisible()).eq(true);
});

When('I try to register a user with details:', async (dataTable: TableDefinition) => {
    const homePageModel = await getPageModel(homePage);
    const registrationModel = await homePageModel.getUserRegistrationModel();

    await registrationModel.populateForm(dataTable.rowsHash());

    await registrationModel.submitForm();
});

Then('I get errors registering a user:', async (dataTable: TableDefinition) => {
    const homePageModel = await getPageModel(homePage);
    const registrationModel = await homePageModel.getUserRegistrationModel();

    await registrationModel.assertFormErrors(dataTable.rowsHash());
});

Then('I get an error registering a user of {string}', async (error: string) => {
    const homePageModel = await getPageModel(homePage);
    const registrationModel = await homePageModel.getUserRegistrationModel();

    chai.expect(await registrationModel.getError()).eq(error);
});
