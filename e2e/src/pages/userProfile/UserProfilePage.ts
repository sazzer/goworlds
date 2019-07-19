import {BasePage} from "../BasePage";
import {By, WebElement} from "selenium-webdriver";
import {EmailEntryModel} from "../authentication/EmailEntryModel";
import {ProfileFormModel} from "./ProfileFormModel";
import {ChangePasswordFormModel} from "./ChangePasswordFormModel";

/**
 * Page Model that represents the User Profile page
 */
export class UserProfilePage extends BasePage {
    /**
     * Construct the page model
     * @param webElement Web Element for the page
     */
    constructor(webElement: WebElement) {
        super(webElement);
    }

    /**
     * Get the form for the user profile
     */
    async getUserProfileForm() : Promise<ProfileFormModel> {
        const userProfileLink = await this.webElement.findElement(By.css('.four.wide.column a[href="/profile"]'));
        await userProfileLink.click();

        const userProfileForm = await this.webElement.findElement(By.css('[data-test="ProfileForm"]'));

        return new ProfileFormModel(userProfileForm);
    }

    /**
     * Get the form for changing the password
     */
    async getChangePasswordForm() : Promise<ChangePasswordFormModel> {
        const changePasswordLink = await this.webElement.findElement(By.css('.four.wide.column a[href="/profile/password"]'));
        await changePasswordLink.click();

        const changePasswordForm = await this.webElement.findElement(By.css('[data-test="PasswordForm"]'));

        return new ChangePasswordFormModel(changePasswordForm);
    }
}

/**
 * Builder function to build the User Profile page
 * @param webElement Web element for the page
 */
export function userProfilePage(webElement: WebElement) : UserProfilePage {
    return new UserProfilePage(webElement);
}
