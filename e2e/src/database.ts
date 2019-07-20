import {AfterAll, Before, BeforeAll} from "cucumber";
import {Pool} from "pg";
import debug from 'debug';

/** The logger to use */
const LOG = debug('goworlds:database');

let pool: Pool;

BeforeAll(async () => {
    const connectionString = process.env.DATABASE_URI as string;

    LOG('Connecting to database: %s', connectionString);

    pool = new Pool({
        connectionString: connectionString
    });

    await pool.query("SELECT 1");
});

Before(async () => {
    const tables = await pool.query('SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = $1', ['public']);

    const tableNames = tables.rows.map(row => row.tablename)
        .filter(table => table !== 'users')
        .filter(table => table !== 'oauth2_clients');

    LOG('Truncating tables: %s', tableNames);
    await pool.query('TRUNCATE ' + tableNames.join(', '));

    // Now delete the users that do *not* have OAuth2 Credentials
    await pool.query('DELETE FROM users WHERE user_id NOT IN (SELECT owner_id FROM oauth2_clients)');
});

AfterAll(async () => {
    LOG('Disconnecting from database');
    await pool.end();
});
