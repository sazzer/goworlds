import {error, WebElement} from "selenium-webdriver";
import {waitUntilAvailable} from "./selenium/waitUtils";

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
}
