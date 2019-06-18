import './i18n';
import 'semantic-ui-css/semantic.min.css';

import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router} from 'react-router-dom';

import {App} from './ui/App';

/**
 * The wrapper around the main application to set everything up
 * @return {*} the main application
 */
const AppWrapper = () => (
    <Router>
        <App />
    </Router>
);

ReactDOM.render(<AppWrapper />, document.getElementById('root'));
