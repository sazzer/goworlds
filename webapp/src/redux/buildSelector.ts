type SelectorTransform = (input: any) => any;

/**
 * Helper to build a selector that only sees a subset of the store
 * @param path the path to the store subset
 * @param transform an optional lambda to transform the part of the state that was selected
 * @return the selector
 */
export function buildSelector(path: string[], transform?: SelectorTransform): (state: any) => any {
    return (state) => {
        const statePart = path.reduce((accum, current) => {
            if (typeof accum === 'object') {
                return accum[current];
            } else {
                return accum;
            }
        }, state);

        if (transform) {
            return transform(statePart);
        } else {
            return statePart;
        }
    }
}
