use super::Database;
use crate::health::Healthcheck;
use log::error;
use postgres::NoTls;
use r2d2_postgres::PostgresConnectionManager;
use std::time::Duration;

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

impl Healthcheck for DatabaseWrapper {
    fn check_health(&self) -> Result<String, String> {
        let timeout = Duration::from_secs(10);
        let mut client = self.pool.get_timeout(timeout).map_err(|e| format!("{}", e))?;
        match client.execute("SELECT 1", &[]) {
            Ok(_) => Ok("Database Connected".to_owned()),
            Err(e) => Err(format!("{}", e)),
        }
    }
}
