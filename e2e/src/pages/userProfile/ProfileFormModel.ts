import {FormModel} from "../FormModel";
import {WebElement} from "selenium-webdriver";

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
}
