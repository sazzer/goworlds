import {FormModel} from "../FormModel";
import {By, WebElement} from "selenium-webdriver";
import {waitUntilAvailable, waitUntilTrue} from "../selenium/waitUtils";
import * as chai from "chai";

/**
 * Page Model that represents the User Profile form
 */
export class ProfileFormModel extends FormModel {
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
        fieldMappings.set('Name', 'name');
        fieldMappings.set('Email Address', 'email');
        return fieldMappings;
    }


    /**
     * Get the text from a global user profile error
     */
    async getError() : Promise<string> {
        const errorField = await waitUntilAvailable(async () =>
            await this.webElement.findElement(By.css('[data-test="ProfileFormErrors"]')));

        const visible = await waitUntilTrue(async () => await errorField.isDisplayed());
        chai.expect(visible, 'Error Message Visibility').eq(true);

        return await errorField.getText();
    }
}
