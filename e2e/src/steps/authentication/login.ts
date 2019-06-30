import {TableDefinition, Then, When} from "cucumber";
import * as chai from 'chai';
import {getPageModel} from "../../browser";
import {homePage} from "../../pages/HomePage";
import {waitUntilTrue} from "../../pages/selenium/waitUtils";

const FIELD_MAPPING = new Map<string, string>();
FIELD_MAPPING.set('Password', 'password');

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
    chai.expect(await loginModel.isVisible()).eq(true);

    const data = dataTable.rowsHash();

    for (const field of Object.keys(data)) {
        const errorMessage = data[field];

        if (errorMessage !== undefined && errorMessage.trim() !== '') {
            const mappedField = FIELD_MAPPING.get(field);

            if (mappedField !== undefined) {
                chai.expect(await waitUntilTrue(() => loginModel.isFieldError(mappedField)), `Field is in error: ${field}`).eq(true);
                chai.expect(await loginModel.getFieldErrorText(mappedField)).eq(errorMessage);
            }
        }
    }

    await loginModel.submitForm();
});

Then('I get an error logging in of {string}', async (error: string) => {
    const homePageModel = await getPageModel(homePage);
    const loginModel = await homePageModel.getLoginModel();

    chai.expect(await loginModel.getError()).eq(error);
});
