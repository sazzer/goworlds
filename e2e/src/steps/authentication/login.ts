import {TableDefinition, Then, When} from "cucumber";
import * as chai from 'chai';
import {getPageModel} from "../../browser";
import {homePage} from "../../pages/HomePage";

Then('the Login form is displayed', async () => {
    const homePageModel = await getPageModel(homePage);
    const loginModel = await homePageModel.getLoginModel();

    chai.expect(await loginModel.isVisible()).eq(true);
});

When('I try to log in with a password of {string}', async (password: string) => {
    const homePageModel = await getPageModel(homePage);
    const loginModel = await homePageModel.getLoginModel();

    await loginModel.setFieldValue('password', password);
    await loginModel.submitForm();
});

Then('I get errors logging in:', async (dataTable: TableDefinition) => {
    const homePageModel = await getPageModel(homePage);
    const loginModel = await homePageModel.getLoginModel();

    await loginModel.assertFormErrors(dataTable.rowsHash());
});

Then('I get an error logging in of {string}', async (error: string) => {
    const homePageModel = await getPageModel(homePage);
    const loginModel = await homePageModel.getLoginModel();

    chai.expect(await loginModel.getError()).eq(error);
});
