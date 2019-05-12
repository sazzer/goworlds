use config::{Config, ConfigError, Environment};
use serde::Deserialize;

/// Settings describe the configuration settings of the application
#[derive(Debug, Deserialize)]
pub struct Settings {
    /// port is the port that the HTTP Server will be listening on
    pub port: u16,
    /// The URI to connect to for the Postgres Database
    pub database_uri: String,
}

impl Settings {
    /// new will create a new Settings object, populating from environment variables and providing defaults where
    /// not otherwise available
    pub fn new() -> Result<Self, ConfigError> {
        let mut s = Config::new();

        s.set_default("PORT", 3000)?;

        s.merge(Environment::default())?;

        s.try_into()
    }
}
