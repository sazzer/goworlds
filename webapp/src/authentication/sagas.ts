import {sagas as checkEmailExists} from "./checkEmailExists";
import {sagas as authenticate} from "./authenticate";
import {sagas as register} from "./register";
import {sagas as accessToken} from "./accessToken";

export default [
    ...checkEmailExists,
    ...authenticate,
    ...register,
    ...accessToken,
];
