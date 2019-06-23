import {sagas as checkEmailExists} from "./checkEmailExists";
import {sagas as authenticate} from "./authenticate";
import {sagas as register} from "./register";

export default [
    ...checkEmailExists,
    ...authenticate,
    ...register,
];
