import { sagas as usersSagas } from './users';
import { sagas as updateProfileSagas } from './updateProfile';

export default [
    ...usersSagas,
    ...updateProfileSagas,
];
