import {error} from "selenium-webdriver";
import NoSuchElementError = error.NoSuchElementError;

/**
 * Call the given function on repeat until it doesn't throw a NoSuchElementError
 * @param fn the function to call
 * @param count the number of times to repeat
 * @param wait the wait between repeats
 * @return the result of the function
 */
export async function waitUntilAvailable<T>(fn: () => Promise<T>, count: number = 10, wait: number = 1000): Promise<T> {
    for (let i = 0; i < count; ++i) {
        try {
            return await fn();
        } catch (e) {
            if (e instanceof NoSuchElementError && i < count) {
                // Wait and retry
                await new Promise((resolve) => setTimeout(resolve, wait));
            } else {
                throw e;
            }
        }
    }

    throw new Error('Failed to find element after timeout');
}
