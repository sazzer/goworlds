import React, {FunctionComponent} from 'react';
import './body.css';

/** The props that a Body needs */
type BodyProps = {};

/**
 * The body of the application
 * @constructor
 */
export const Body: FunctionComponent<BodyProps> = ({children}) => (
    <div id="appBody">
            {children}
    </div>
);
