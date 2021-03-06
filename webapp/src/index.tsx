import './i18n';
import 'semantic-ui-css/semantic.min.css';

import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {BrowserRouter as Router} from 'react-router-dom';
import {buildStore} from './redux/store';

import {App} from './ui/App';

/**
 * The wrapper around the main application to set everything up
 * @return {*} the main application
 */
const AppWrapper = () => (
    <Router>
        <Provider store={buildStore()}>
            <App />
        </Provider>
    </Router>
);

ReactDOM.render(<AppWrapper />, document.getElementById('root'));
