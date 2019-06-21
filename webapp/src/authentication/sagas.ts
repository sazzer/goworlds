import {sagas as checkEmailExists} from "./checkEmailExists";
import {sagas as authenticate} from "./authenticate";

export default [
    ...checkEmailExists,
    ...authenticate,
];
