import {error} from "selenium-webdriver";
import NoSuchElementError = error.NoSuchElementError;

/**
 * Wait a period of time
 * @param wait the number of millis to wait
 */
export function waitTime(wait: number = 100) {
    return new Promise((resolve) => setTimeout(resolve, wait));
}

/**
 * Call the given function on repeat until it doesn't throw a NoSuchElementError
 * @param fn the function to call
 * @param count the number of times to repeat
 * @param wait the wait between repeats
 * @return the result of the function
 */
export async function waitUntilAvailable<T>(fn: () => Promise<T>, count: number = 10, wait: number = 100): Promise<T> {
    for (let i = 0; i < count; ++i) {
        try {
            return await fn();
        } catch (e) {
            if (e instanceof NoSuchElementError && i < count) {
                // Wait and retry
                await waitTime(wait);
            } else {
                throw e;
            }
        }
    }

    throw new Error('Failed to find element after timeout');
}


/**
 * Call the given function on repeat until it does throw a NoSuchElementError
 * @param fn the function to call
 * @param count the number of times to repeat
 * @param wait the wait between repeats
 */
export async function waitUntilUnavailable<T>(fn: () => Promise<T>, count: number = 10, wait: number = 100): Promise<any> {
    for (let i = 0; i < count; ++i) {
        try {
            await fn();
            await waitTime(wait);
        } catch (e) {
            if (e instanceof NoSuchElementError && i < count) {
                // Wait and retry
                return;
            } else {
                throw e;
            }
        }
    }

    throw new Error('Element still present after timeout');
}


/**
 * Call the given function on repeat until it returns true
 * @param fn the function to call
 * @param count the number of times to repeat
 * @param wait the wait between repeats
 * @return the result of the function
 */
export async function waitUntilTrue<T>(fn: () => Promise<boolean>, count: number = 10, wait: number = 1000): Promise<boolean> {
    for (let i = 0; i < count; ++i) {
        try {
            const result = await fn();
            if (result) {
                return result;
            }
        } catch (e) {
            if (e instanceof NoSuchElementError && i < count) {
                // Wait and retry
            } else {
                throw e;
            }
        }

        await waitTime(wait);
    }

    return false;
}
