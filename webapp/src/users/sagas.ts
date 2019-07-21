import { sagas as usersSagas } from './users';
import { sagas as updateProfileSagas } from './updateProfile';
import { sagas as changePasswordSagas } from './changePassword';

export default [
    ...usersSagas,
    ...updateProfileSagas,
    ...changePasswordSagas,
];
