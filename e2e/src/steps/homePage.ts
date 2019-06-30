import {Then, When} from "cucumber";
import * as chai from 'chai';
import {getPageModel, openPage} from "../browser";
import {homePage} from "../pages/HomePage";

When('I load the home page', async () => {
    await openPage(process.env.WEBAPP_URL as string);
});

Then('the page header reads {string}', async (pageName: string) => {
    const homePageModel = await getPageModel(homePage);
    const pageTitle = await homePageModel.getPageTitle();

    chai.expect(pageTitle).eq(pageName);
});
