import {Then, When} from "cucumber";
import * as chai from 'chai';
import {getPageModel} from "../../browser";
import {homePage} from "../../pages/HomePage";

When('I try to authenticate with an email address of {string}', async (email: string) => {
    const homePageModel = await getPageModel(homePage);
    const emailEntryModel = await homePageModel.getEmailEntryModel();

    await emailEntryModel.setFieldValue('email', email);
    await emailEntryModel.submitForm();
});

Then('I get an error entering the email address of {string}', async (error: string) => {
    const homePageModel = await getPageModel(homePage);
    const emailEntryModel = await homePageModel.getEmailEntryModel();

    chai.expect(await emailEntryModel.isFieldError('email')).eq(true);
    chai.expect(await emailEntryModel.getFieldErrorText('email')).eq(error);
});

Then('the User Registration form is displayed', async () => {
    const homePageModel = await getPageModel(homePage);
    const registrationModel = await homePageModel.getUserRegistrationModel();

    chai.expect(await registrationModel.isVisible()).eq(true);
});
