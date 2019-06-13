import {reducers as demo} from "./demoModule";
import authentication from "../authentication/reducers";

/**
 * The set of reducers to use
 */
export default {
    demo,
    ...authentication,
};
