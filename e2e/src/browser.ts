import {After, AfterAll, BeforeAll} from 'cucumber';
import {Builder, By, ThenableWebDriver, WebElement} from "selenium-webdriver";
import {BasePage} from "./pages/BasePage";

/** The actual web driver */
let driver: ThenableWebDriver;

BeforeAll(() => {
    const browser = process.env.SELENIUM_BROWSER as string;

    console.log('Loading browser: ' + browser);
    driver = new Builder()
        .forBrowser(browser)
        .build();
});

AfterAll(async () => {
    console.log('Killing browser');
    await driver.quit();
});

After(async function () {
    const screenshot = await driver.takeScreenshot();
    this.attach(screenshot, 'image/png');
});

/**
 * Open the web browser to the given URL
 * @param url the URL
 */
export async function openPage(url: string) {
    await driver.get(url);
}

/**
 * Build a page model using the Web Driver
 * @param pageBuilder The builder for the root page model
 * @return the page model
 */
export async function getPageModel<T extends BasePage>(pageBuilder: (base: WebElement) => T) : Promise<T> {
    const rootElement: WebElement = await driver.findElement(By.id("root"));

    return pageBuilder(rootElement);
}
