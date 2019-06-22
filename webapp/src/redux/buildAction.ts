/** Base for actions */
export interface BaseAction {
    type: string,
}

/** The shape of an action */
export interface Action<T> extends BaseAction {
    payload: T
}

/**
 * Build a Redux Action for the given type and payload
 * @param type The action type
 * @param payload The action payload
 */
export function buildAction<T>(type: string, payload: T) : Action<T>{
    return {
        type,
        payload
    };
}
