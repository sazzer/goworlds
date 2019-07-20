import {Given, TableDefinition} from "cucumber";
import axios from 'axios';
import debug from 'debug';

/** The logger to use */
const LOG = debug('goworlds:user:seed');

const FIELD_MAPPING = new Map<string, string>();
FIELD_MAPPING.set('Email Address', 'email');
FIELD_MAPPING.set('Name', 'name');
FIELD_MAPPING.set('Password', 'password');

Given('a user exists with details:', async (dataTable: TableDefinition) => {
    const user: any = {
        name: 'Test User',
        email: 'test@example.com',
        password: 'superSecretPassword'
    };

    const data = dataTable.rowsHash();

    for (const field of Object.keys(data)) {
        const fieldValue = data[field];

        const mappedField = FIELD_MAPPING.get(field);

        if (mappedField !== undefined) {
            user[mappedField] = fieldValue;
        }
    }
    LOG('Seeding user details: %o', user);

    const response = await axios('/users', {
        method: 'POST',
        baseURL: process.env.SERVICE_URI as string,
        data: user
    });
});
