import {BasePage} from "./BasePage";
import {By, WebElement, Key} from "selenium-webdriver";
import * as chai from "chai";
import {waitUntilTrue} from "./selenium/waitUtils";
import {waitUntilClassIsAbsent} from "./selenium/cssUtils";

/**
 * Page Model that represents a form
 */
export class FormModel extends BasePage {
    /**
     * Construct the page model
     * @param webElement Web Element for the page
     */
    constructor(webElement: WebElement) {
        super(webElement);
    }

    /**
     * Wait until the form has finished loading
     */
    protected async isLoaded() {
        await waitUntilClassIsAbsent(this.webElement, 'loading');
    }

    /**
     * Set the value of a field
     * @param field the name of the field
     * @param value the new value for the field
     */
    async setFieldValue(field: string, value: string) {
        await this.isLoaded();
        const input = await this.webElement.findElement(By.css(`[data-test="${field}"] input`));

        await input.click();

        // input.clear() should be used here. It doesn't work. So instead we do it manually :(
        while (true) {
            const currentValue = await input.getAttribute('value');
            if (currentValue === '') {
                break;
            }
            await input.sendKeys(Key.END);
            for (let i = 0; i < currentValue.length; ++i) {
                await input.sendKeys(Key.BACK_SPACE);
            }
        }

        await input.sendKeys(value);

        const label = await this.webElement.findElement(By.css(`[data-test="${field}"] label`));
        await label.click();
    }
    
    /**
     * Get the value of a field
     * @param field the name of the field
     */
    async getFieldValue(field: string) : Promise<string> {
        await this.isLoaded();
        const input = await this.webElement.findElement(By.css(`[data-test="${field}"] input`));

        return await input.getAttribute('value');
    }

    /**
     * Check if a field is currently in error
     * @param field the name of the field
     */
    async isFieldError(field: string) : Promise<boolean> {
        await this.isLoaded();
        const fieldElement = await this.webElement.findElement(By.css(`[data-test="${field}"] .field`));

        const className = await fieldElement.getAttribute('class');
        const splitClassNames = className.split(' ');

        return splitClassNames.includes('error');
    }

    /**
     * Get the text of an error on a field
     * @param field the name of the field
     */
    async getFieldErrorText(field: string) : Promise<string> {
        await this.isLoaded();
        const fieldError = await this.webElement.findElement(By.css(`[data-test="${field}"] div.error.message`));

        return await fieldError.getText();
    }

    /**
     * Submit the form
     */
    async submitForm() {
        await this.isLoaded();
        const submit = await this.webElement.findElement(By.css('button.primary'));

        await submit.click();
    }

    /**
     * Get the mappings from Cucumber fields to HTML Fields
     */
    protected getFieldMappings() : Map<string, string> {
        return new Map<string, string>();
    }

    private async processForm(data: { [key: string]: string }, processor: (mappedField: string, value: string) => Promise<void>) {
        chai.expect(await this.isVisible()).eq(true);
        await this.isLoaded();
        const fieldMappings = this.getFieldMappings();

        for (const field of Object.keys(data)) {
            const fieldValue = data[field];

            const mappedField = fieldMappings.get(field);

            if (mappedField !== undefined) {
                await processor(mappedField, fieldValue);
            }
        }
    }

    /**
     * Populate the form with the provided data
     */
    async populateForm(data: { [key: string]: string }) {
        await this.processForm(data, async (mappedField, fieldValue) => {
            await this.setFieldValue(mappedField, fieldValue);
        });
    }

    /**
     * Assert the form has the provided data
     */
    async assertFormData(data: { [key: string]: string }) {
        await this.processForm(data, async (mappedField, fieldValue) => {
            let value = await this.getFieldValue(mappedField);
            chai.expect(value).eq(fieldValue);
        });
    }

    /**
     * Assert the form has the provided errors
     */
    async assertFormErrors(data: { [key: string]: string }) {
        await this.processForm(data, async (mappedField, errorMessage) => {
            if (errorMessage.trim() !== '') {
                chai.expect(await waitUntilTrue(() => this.isFieldError(mappedField)), `Field is in error: ${mappedField}`).eq(true);
                chai.expect(await this.getFieldErrorText(mappedField)).eq(errorMessage);
            }
        });
    }
}
