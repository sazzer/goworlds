use super::Database;
use log::error;
use postgres::NoTls;
use r2d2_postgres::PostgresConnectionManager;

/// A wrapper around a connection pool with which we can actually talk to the database
pub struct DatabaseWrapper {
    pool: r2d2::Pool<PostgresConnectionManager<NoTls>>,
}

/// Possible errors when connecting to the databae
#[derive(Debug)]
pub enum DatabaseConnectionError {
    InvalidUrl,
    ConnectionError,
}

impl DatabaseWrapper {
    /// Create a new wrapper around the database connection
    pub fn new(url: String) -> Result<DatabaseWrapper, DatabaseConnectionError> {
        let parsed_url = match url.parse() {
            Err(e) => {
                error!("Failed to parse connection string: {}", e);
                return Err(DatabaseConnectionError::InvalidUrl);
            }
            Ok(u) => u,
        };

        let manager = PostgresConnectionManager::new(parsed_url, NoTls);
        let pool = match r2d2::Pool::builder()
            .max_size(10)
            .min_idle(Some(1))
            .build(manager)
        {
            Err(e) => {
                error!("Failed to create connection pool: {}", e);
                return Err(DatabaseConnectionError::ConnectionError);
            }
            Ok(p) => p,
        };

        Ok(DatabaseWrapper { pool: pool })
    }
}

impl Database for DatabaseWrapper {}
