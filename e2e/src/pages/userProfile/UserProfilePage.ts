import {BasePage} from "../BasePage";
import {By, WebElement} from "selenium-webdriver";
import {EmailEntryModel} from "../authentication/EmailEntryModel";
import {ProfileFormModel} from "./ProfileFormModel";

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
        const userProfileForm = await this.webElement.findElement(By.css('[data-test="ProfileForm"]'));

        return new ProfileFormModel(userProfileForm);
    }
}

/**
 * Builder function to build the User Profile page
 * @param webElement Web element for the page
 */
export function userProfilePage(webElement: WebElement) : UserProfilePage {
    return new UserProfilePage(webElement);
}
