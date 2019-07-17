import {TableDefinition, Then, When} from "cucumber";

import {getPageModel} from "../../browser";
import {userProfilePage} from "../../pages/userProfile/UserProfilePage";
import * as chai from "chai";

const FIELD_MAPPING = new Map<string, string>();
FIELD_MAPPING.set('Name', 'name');
FIELD_MAPPING.set('Email Address', 'email');

Then('The User Profile Form has details:', async (dataTable: TableDefinition) => {
    const userProfilePageModel = await getPageModel(userProfilePage);
    let userProfileForm = await userProfilePageModel.getUserProfileForm();
    chai.expect(await userProfileForm.isVisible()).eq(true);

    const data = dataTable.rowsHash();

    for (const field of Object.keys(data)) {
        const fieldValue = data[field];

        const mappedField = FIELD_MAPPING.get(field);

        if (mappedField !== undefined) {
            let value = await userProfileForm.getFieldValue(mappedField);
            chai.expect(value).eq(fieldValue);
        }
    }
});

When('I update the User Profile form to:', async (dataTable: TableDefinition) => {
    const userProfilePageModel = await getPageModel(userProfilePage);
    let userProfileForm = await userProfilePageModel.getUserProfileForm();
    chai.expect(await userProfileForm.isVisible()).eq(true);

    const data = dataTable.rowsHash();

    for (const field of Object.keys(data)) {
        const fieldValue = data[field];

        const mappedField = FIELD_MAPPING.get(field);

        if (mappedField !== undefined) {
            await userProfileForm.setFieldValue(mappedField, fieldValue);
        }
    }

    await userProfileForm.submitForm();
});
