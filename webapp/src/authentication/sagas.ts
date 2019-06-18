import {sagas as checkEmailExists} from "./checkEmailExists";
import {sagas as createUser} from './createUser';

export default [
    ...checkEmailExists,
    ...createUser
];
