import 'selenium-webdriver/chrome';
import 'selenium-webdriver/firefox';
import {openPage} from "../browser";
import {homePage} from "../pages/HomePage";

it('Can open the home page correctly', async () => {
    const homePageModel = await openPage(process.env.WEBAPP_URL as string, homePage);

    const title = await homePageModel.getPageTitle();
    expect(title).toEqual('Application Name');
});
