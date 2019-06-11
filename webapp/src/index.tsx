import './i18n';
import 'semantic-ui-css/semantic.min.css';

import React from 'react';
import ReactDOM from 'react-dom';

import App from './ui/App';

/**
 * The wrapper around the main application to set everything up
 * @return {*} the main application
 */
const AppWrapper = () => (
    <App />
);

ReactDOM.render(<AppWrapper />, document.getElementById('root'));
