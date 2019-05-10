use serde::Deserialize;
use config::{Config, ConfigError, Environment};

/// Settings describe the configuration settings of the application
#[derive(Debug, Deserialize)]
pub struct Settings {
  /// port is the port that the HTTP Server will be listening on
  pub port: u16,
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
