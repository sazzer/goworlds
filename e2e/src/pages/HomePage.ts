import {BasePage} from "./BasePage";
import {By, WebElement} from "selenium-webdriver";
import {EmailEntryModel} from "./authentication/EmailEntryModel";
import {UserRegistrationModel} from "./authentication/UserRegistrationModel";
import {waitUntilAvailable} from "./selenium/waitUtils";
import {LoginModel} from "./authentication/LoginModel";

/**
 * Page Model that represents the home page
 */
export class HomePage extends BasePage {
    /**
     * Construct the page model
     * @param webElement Web Element for the page
     */
    constructor(webElement: WebElement) {
        super(webElement);
    }

    /**
     * Get the title of the page
     */
    async getPageTitle() : Promise<String> {
        const titleElement = await this.webElement.findElement(By.css('div.ui.fluid.inverted.top.fixed.menu > div'));

        return await titleElement.getText();
    }

    /**
     * Get the form for entering an email address
     */
    async getEmailEntryModel() : Promise<EmailEntryModel> {
        const emailEntryForm = await this.webElement.findElement(By.css('[data-test="EmailEntry"]'));

        return new EmailEntryModel(emailEntryForm);
    }

    /**
     * Get the form for registering a user
     */
    async getUserRegistrationModel() : Promise<UserRegistrationModel> {
        return waitUntilAvailable(async () => {
            const userRegistrationForm = await this.webElement.findElement(By.css('[data-test="Register"]'));

            return new UserRegistrationModel(userRegistrationForm);
        });
    }

    /**
     * Get the form for logging in to an existing user
     */
    async getLoginModel() : Promise<LoginModel> {
        return waitUntilAvailable(async () => {
            const loginForm = await this.webElement.findElement(By.css('[data-test="Login"]'));

            return new LoginModel(loginForm);
        });
    }
}

/**
 * Builder function to build the home page
 * @param webElement Web element for the page
 */
export function homePage(webElement: WebElement) : HomePage {
    return new HomePage(webElement);
}
