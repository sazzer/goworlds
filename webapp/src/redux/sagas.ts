import {sagas as demoSagas} from './demoModule';
import authenticationSagas from "../authentication/sagas";

/**
 * The set of Sagas to use
 */
export default [
    ...demoSagas,
    ...authenticationSagas,
];
