import {After, AfterAll, BeforeAll} from 'cucumber';
import {Builder, By, logging, ThenableWebDriver, WebElement} from "selenium-webdriver";
import {BasePage} from "./pages/BasePage";
import debug from 'debug';

/** The logger to use */
const LOG = debug('goworlds:browser');

/** The actual web driver */
let driver: ThenableWebDriver;

BeforeAll(() => {
    const browser = process.env.SELENIUM_BROWSER as string;

    LOG('Loading browser: %s', browser);
    driver = new Builder()
        .forBrowser(browser)
        .build();
});

AfterAll(async () => {
    LOG('Killing browser');
    await driver.quit();
});

After(async function () {
    const screenshot = await driver.takeScreenshot();
    this.attach(screenshot, 'image/png');

    const logEntries = await driver.manage().logs().get(logging.Type.BROWSER);

    const log = logEntries.map(entry => `[${entry.level.name}] ${entry.timestamp} - ${entry.message}`)
        .join('\n');
    this.attach(log, 'text/plain');
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
