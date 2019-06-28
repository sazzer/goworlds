import {BasePage} from "./BasePage";
import {By, WebElement} from "selenium-webdriver";

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
     * Set the value of a field
     * @param field the name of the field
     * @param value the new value for the field
     */
    async setFieldValue(field: string, value: string) {
        const emailInput = await this.webElement.findElement(By.css(`[data-test="${field}"] input`));

        await emailInput.clear();
        await emailInput.sendKeys(value);
    }

    /**
     * Check if a field is currently in error
     * @param field the name of the field
     */
    async isFieldError(field: string) : Promise<boolean> {
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
        const fieldError = await this.webElement.findElement(By.css(`[data-test="${field}"] div.error.message`));

        return await fieldError.getText();
    }

    /**
     * Submit the form
     */
    async submitForm() {
        const submit = await this.webElement.findElement(By.css('button.primary'));

        await submit.click();
    }
}
