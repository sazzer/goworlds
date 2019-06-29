import {FormModel} from "../FormModel";
import {WebElement} from "selenium-webdriver";

/**
 * Page Model that represents the Login form
 */
export class LoginModel extends FormModel {
    /**
     * Construct the page model
     * @param webElement Web Element for the page
     */
    constructor(webElement: WebElement) {
        super(webElement);
    }
}
