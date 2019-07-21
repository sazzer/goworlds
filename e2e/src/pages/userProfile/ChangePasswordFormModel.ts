import {FormModel} from "../FormModel";
import {By, WebElement} from "selenium-webdriver";
import {waitUntilAvailable} from "../selenium/waitUtils";
import debug from 'debug';

/** The logger to use */
const LOG = debug('goworlds:ChangePasswordFormModel');

/**
 * Page Model that represents the Change Password form
 */
export class ChangePasswordFormModel extends FormModel {
    /**
     * Construct the page model
     * @param webElement Web Element for the page
     */
    constructor(webElement: WebElement) {
        super(webElement);
    }

    /**
     * Get the mappings from Cucumber fields to HTML Fields
     */
    protected getFieldMappings() : Map<string, string> {
        const fieldMappings = new Map<string, string>();
        fieldMappings.set('Password', 'password');
        fieldMappings.set('Re-enter Password', 'password2');
        return fieldMappings;
    }

    /**
     * Return whether the Success Message is visible in the UI
     */
    async hasSuccessMessage() : Promise<boolean> {
        LOG('Waiting for the Success Message to be displayed');
        return waitUntilAvailable(async () => {
            const successMessageElement = await this.webElement.findElement(By.css('.ui.positive.message'));
            await successMessageElement.isDisplayed();
            return await this.webElement.isDisplayed();
        });
    }
}
