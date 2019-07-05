import authenticationSagas from "../authentication/sagas";
import userSagas from '../users/sagas';
/**
 * The set of Sagas to use
 */
export default [
    ...authenticationSagas,
    ...userSagas,
];
