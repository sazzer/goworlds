use super::Database;
use crate::health::Healthcheck;
use dbmigrate_lib::{get_driver, read_migration_files};
use log::{debug, error, info};
use postgres::NoTls;
use r2d2_postgres::PostgresConnectionManager;
use std::path::Path;
use std::time::Duration;

/// A wrapper around a connection pool with which we can actually talk to the database
pub struct DatabaseWrapper {
    uri: String,
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
    pub fn new(uri: String) -> Result<DatabaseWrapper, DatabaseConnectionError> {
        let parsed_url = match uri.parse() {
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

        Ok(DatabaseWrapper { uri: uri, pool: pool })
    }

    pub fn migrate(&self) -> Result<(), String> {
        let driver = get_driver(&self.uri).map_err(|e| format!("Failed to get driver: {}", e))?;

        let migrations_path = Path::new("./migrations");
        debug!("Loading migrations from {}", migrations_path.canonicalize().unwrap().display());

        let migration_files =
            read_migration_files(migrations_path).map_err(|e| format!("Failed to get migrations: {}", e))?;

        let current = driver.get_current_number();
        let max = migration_files.keys().max().unwrap_or(&0i32);

        if current == *max {
            info!("Migrations are up-to-date");
            return Ok(());
        }

        info!("Migrations applied: {} of {}", current, max);

        for (number, migration) in migration_files.iter() {
            if number > &current {
                let mig_file = migration.up.as_ref().unwrap();
                info!("Applying migration {}: {}", number, mig_file.name);
                let content = mig_file.content.clone()
                    .ok_or(format!("Failed to read migation file {}", mig_file.name))?;

                match driver.migrate(content, mig_file.number) {
                    Err(e) => {
                        error!("Failed to apply migration {}: {}", mig_file.name, e);
                        return Err(format!("Failed to apply migration {}: {}", mig_file.name, e));
                    }
                    Ok(_) => {
                        info!("Applied migration {}: {}", number, mig_file.name);
                    }
                }
            }
        }

        Ok(())
    }

}

impl Database for DatabaseWrapper {}

impl Healthcheck for DatabaseWrapper {
    fn check_health(&self) -> Result<String, String> {
        let timeout = Duration::from_secs(10);
        let mut client = self
            .pool
            .get_timeout(timeout)
            .map_err(|e| format!("{}", e))?;
        match client.execute("SELECT 1", &[]) {
            Ok(_) => Ok("Database Connected".to_owned()),
            Err(e) => Err(format!("{}", e)),
        }
    }
}
