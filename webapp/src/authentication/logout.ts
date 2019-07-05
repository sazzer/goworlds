import {MODULE_PREFIX} from "./module";
import {Action, buildAction} from "../redux";

//////// Action for logging out

/** The action type for logging out */
export const LOGOUT_ACTION = MODULE_PREFIX + 'logout';

/** The shape of the action to log out */
declare type LogoutAction = {};

/**
 * Build the Redux action to log out
 */
export function logout(): Action<LogoutAction> {
    return buildAction(LOGOUT_ACTION, {});
}
