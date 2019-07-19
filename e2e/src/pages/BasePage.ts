import {By, error, WebElement} from "selenium-webdriver";
import {waitUntilAvailable, waitUntilUnavailable} from "./selenium/waitUtils";
import {HomePage} from "./HomePage";
import {UserMenuModel} from "./header/UserMenuModel";

/**
 * Base class for page models
 */
export class BasePage {
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
     * Check if this page model is currently visible
     */
    async isVisible() : Promise<boolean> {
        return waitUntilAvailable(async () => {
            return await this.webElement.isDisplayed();
        });
    }

    /**
     * Get the User Menu, once it's available
     */
    async getUserMenu() : Promise<UserMenuModel> {
        return await waitUntilAvailable(async () => {
            const userMenuElement = await this.webElement.findElement(By.css('.top.fixed.menu .right.menu .dropdown'));
            await userMenuElement.isDisplayed();

            return new UserMenuModel(userMenuElement);
        });
    }

    /**
     * Return whether the User Menu is present in the UI
     */
    async userMenuNotPresent() : Promise<boolean> {
        await waitUntilUnavailable(async () => {
            const userMenuElement = await this.webElement.findElement(By.css('.top.fixed.menu .right.menu .dropdown'));
            await userMenuElement.isDisplayed();
        });
        return true;
    }
}

/**
 * Builder function to build the base page, for arbitrary page stuff
 * @param webElement Web element for the page
 */
export function basePage(webElement: WebElement) : BasePage {
    return new BasePage(webElement);
}
