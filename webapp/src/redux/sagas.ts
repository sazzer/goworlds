import {sagas as demoSagas} from './demoModule';
import {sagas as authenticationSagas} from "../authentication";

/**
 * The set of Sagas to use
 */
export default [
    ...demoSagas,
    ...authenticationSagas,
];
