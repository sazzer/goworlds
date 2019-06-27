import {WebElement} from "selenium-webdriver";

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

}
