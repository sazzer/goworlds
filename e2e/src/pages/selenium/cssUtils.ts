import {WebElement} from "selenium-webdriver";
import {waitUntilTrue} from "./waitUtils";
import debug from 'debug';

/** The logger to use */
const LOG = debug('goworlds:cssUtils');

/**
 * Check if a Web Element has the given CSS class name present
 * @param webElement the Web Element
 * @param className the CSS Class Name
 */
export async function classIsPresent(webElement: WebElement, className: string) : Promise<boolean> {
    const classNames = await webElement.getAttribute('class');
    const splitNames = classNames.split( ' ');
    LOG('Looking for CSS Class name %s in names %s', className, splitNames);

    const classNamePresent = splitNames.includes(className);

    return classNamePresent;
}

/**
 * Check if a Web Element has the given CSS class name absent
 * @param webElement the Web Element
 * @param className the CSS Class Name
 */
export async function classIsAbsent(webElement: WebElement, className: string) : Promise<boolean> {
    LOG('Checking if CSS class is absent: %s', className);
    return !await classIsPresent(webElement, className);
}

/**
 * Wait until the given CSS Class is present on the Web Element
 * @param webElement the Web Element
 * @param className the CSS Class Name
 */
export async function waitUntilClassIsPresent(webElement: WebElement, className: string) {
    LOG('Checking if CSS class is present: %s', className);
    await waitUntilTrue(() => classIsPresent(webElement, className));
}

/**
 * Wait until the given CSS Class is absent on the Web Element
 * @param webElement the Web Element
 * @param className the CSS Class Name
 */
export async function waitUntilClassIsAbsent(webElement: WebElement, className: string) {
    LOG('Waiting for CSS class to be absent: %s', className);
    await waitUntilTrue(() => classIsAbsent(webElement, className));
}
