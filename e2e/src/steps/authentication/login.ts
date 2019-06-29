import {Then} from "cucumber";
import * as chai from 'chai';

import {getPageModel} from "../../browser";
import {homePage} from "../../pages/HomePage";

Then('the Login form is displayed', async () => {
    const homePageModel = await getPageModel(homePage);
    const loginModel = await homePageModel.getLoginModel();

    chai.expect(await loginModel.isVisible()).eq(true);
});
