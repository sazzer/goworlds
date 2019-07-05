import authentication from "../authentication/reducers";
import users from '../users/reducers';

/**
 * The set of reducers to use
 */
export default {
    ...authentication,
    ...users,
};
