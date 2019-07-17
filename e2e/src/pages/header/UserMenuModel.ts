import {By, WebElement} from "selenium-webdriver";

/**
 * Page Model representing the User Menu
 */
export class UserMenuModel {
    /** The Web Element for this page model */
    protected webElement: WebElement;

    /**
     * Construct the page model
     * @param webElement the Web Element that represents this page model
     */
    constructor(webElement: WebElement) {
        this.webElement = webElement;
    }

    /**
     * Get the name of the logged in user
     */
    async getUserName() : Promise<string> {
        const element = await this.webElement.findElement(By.css('.text[role="alert"]'));

        return await element.getText();
    }
}
