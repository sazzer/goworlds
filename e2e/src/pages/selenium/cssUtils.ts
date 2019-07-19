import {WebElement} from "selenium-webdriver";
import {waitTime, waitUntilTrue} from "./waitUtils";

/**
 * Check if a Web Element has the given CSS class name present
 * @param webElement the Web Element
 * @param className the CSS Class Name
 */
export async function classIsPresent(webElement: WebElement, className: string) : Promise<boolean> {
    const classNames = await webElement.getAttribute('class');
    const splitNames = classNames.split( ' ');
    const classNamePresent = splitNames.includes(className);

    return classNamePresent;
}

/**
 * Check if a Web Element has the given CSS class name absent
 * @param webElement the Web Element
 * @param className the CSS Class Name
 */
export async function classIsAbsent(webElement: WebElement, className: string) : Promise<boolean> {
    return !await classIsPresent(webElement, className);
}

/**
 * Wait until the given CSS Class is present on the Web Element
 * @param webElement the Web Element
 * @param className the CSS Class Name
 */
export async function waitUntilClassIsPresent(webElement: WebElement, className: string) {
    await waitUntilTrue(() => classIsPresent(webElement, className));
}

/**
 * Wait until the given CSS Class is absent on the Web Element
 * @param webElement the Web Element
 * @param className the CSS Class Name
 */
export async function waitUntilClassIsAbsent(webElement: WebElement, className: string) {
    await waitUntilTrue(() => classIsAbsent(webElement, className));
}