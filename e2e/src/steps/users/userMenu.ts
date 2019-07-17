import {Then, When} from "cucumber";
import * as chai from 'chai';
import {getPageModel} from "../../browser";
import {basePage} from "../../pages/BasePage";

Then('I am logged in as {string}', async (name: string) => {
    const basePageModel = await getPageModel(basePage);
    const userMenu = await basePageModel.getUserMenu();

    chai.expect(await userMenu.getUserName()).eq(name);
});

When('I open the User Profile page', async () => {
    const basePageModel = await getPageModel(basePage);
    const userMenu = await basePageModel.getUserMenu();

    await userMenu.openUserProfile();
});