import {TableDefinition, Then, When} from "cucumber";
import * as chai from 'chai';

import {getPageModel} from "../../browser";
import {homePage} from "../../pages/HomePage";
import {waitUntilTrue} from "../../pages/selenium/waitUtils";

const FIELD_MAPPING = new Map<string, string>();
FIELD_MAPPING.set('Name', 'name');
FIELD_MAPPING.set('Password', 'password');
FIELD_MAPPING.set('Re-enter Password', 'password2');

When('I try to register a user with details:', async (dataTable: TableDefinition) => {
    const homePageModel = await getPageModel(homePage);
    const registrationModel = await homePageModel.getUserRegistrationModel();
    chai.expect(await registrationModel.isVisible()).eq(true);

    const data = dataTable.rowsHash();

    for (const field of Object.keys(data)) {
        const fieldValue = data[field];

        const mappedField = FIELD_MAPPING.get(field);

        if (mappedField !== undefined) {
            await registrationModel.setFieldValue(mappedField, fieldValue);
        }
    }

    await registrationModel.submitForm();
});

Then('I get errors registering a user:', async (dataTable: TableDefinition) => {
    const homePageModel = await getPageModel(homePage);
    const registrationModel = await homePageModel.getUserRegistrationModel();
    chai.expect(await registrationModel.isVisible()).eq(true);

    const data = dataTable.rowsHash();

    for (const field of Object.keys(data)) {
        const errorMessage = data[field];

        if (errorMessage !== undefined && errorMessage.trim() !== '') {
            const mappedField = FIELD_MAPPING.get(field);

            if (mappedField !== undefined) {
                chai.expect(await waitUntilTrue(() => registrationModel.isFieldError(mappedField)), `Field is in error: ${field}`).eq(true);
                chai.expect(await registrationModel.getFieldErrorText(mappedField)).eq(errorMessage);
            }
        }
    }

    await registrationModel.submitForm();
});
