import {Builder, By, ThenableWebDriver, WebElement} from "selenium-webdriver";
import {BasePage} from "./pages/BasePage";

/** The actual web driver */
let driver: ThenableWebDriver;

beforeAll(() => {
    console.log('Opening Web Driver');
    driver = new Builder()
        .forBrowser(process.env.SELENIUM_BROWSER as string)
        .build();

});

afterAll(async () => {
    console.log('Closing Web Driver');
    await driver.quit();
});

/**
 * Open the web browser to the given URL
 * @param url the URL
 * @param pageBuilder The builder for the root page model
 * @return the page model
 */
export async function openPage<T extends BasePage>(url: string, pageBuilder: (base: WebElement) => T) : Promise<T> {
    await driver.get(url);
    const rootElement: WebElement = await driver.findElement(By.id("root"));

    return pageBuilder(rootElement);
}
