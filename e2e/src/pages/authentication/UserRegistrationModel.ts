import {FormModel} from "../FormModel";
import {By, WebElement} from "selenium-webdriver";
import * as chai from 'chai';
import {waitUntilAvailable, waitUntilTrue} from "../selenium/waitUtils";

/**
 * Page Model that represents the User Registration form
 */
export class UserRegistrationModel extends FormModel {
    /**
     * Construct the page model
     * @param webElement Web Element for the page
     */
    constructor(webElement: WebElement) {
        super(webElement);
    }

    /**
     * Get the text from a global user registration error
     */
    async getError() : Promise<string> {
        const errorField = await waitUntilAvailable(async () =>
            await this.webElement.findElement(By.css('[data-test="RegisterErrors"]')));

        const visible = await waitUntilTrue(async () => await errorField.isDisplayed());
        chai.expect(visible, 'Error Message Visibility').eq(true);

        return await errorField.getText();
    }

    /**
     * Get the mappings from Cucumber fields to HTML Fields
     */
    protected getFieldMappings() : Map<string, string> {
        const fieldMappings = new Map<string, string>();
        fieldMappings.set('Name', 'name');
        fieldMappings.set('Password', 'password');
        fieldMappings.set('Re-enter Password', 'password2');
        return fieldMappings;
    }
}
