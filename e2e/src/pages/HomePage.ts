import {BasePage} from "./BasePage";
import {By, WebElement} from "selenium-webdriver";

/**
 * Page Model that represents the home page
 */
export class HomePage extends BasePage {
    /**
     * Construct the page model
     * @param webElement Web Element for the page
     */
    constructor(webElement: WebElement) {
        super(webElement);
    }

    async getPageTitle() : Promise<String> {
        const titleElement = await this.webElement.findElement(By.css('div.ui.fluid.inverted.top.fixed.menu > div'));

        return await titleElement.getText();
    }
}

/**
 * Builder function to build the home page
 * @param webElement Web element for the page
 */
export function homePage(webElement: WebElement) : HomePage {
    return new HomePage(webElement);
}
