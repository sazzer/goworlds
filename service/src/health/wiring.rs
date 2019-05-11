use std::sync::Arc;
use log::debug;
use {super::Healthcheck, super::Healthchecker};
use {crate::server::ServerConfigurer};
use actix_web::web::ServiceConfig;

/// The details of the healthchecks for wiring the app together
pub struct Wiring {
  healthchecker: Healthchecker,
}

impl Wiring {
  // Add a new check to be performed
  pub fn add_check(&mut self, name: String, check: Arc<Healthcheck>) -> &mut Self {
    debug!("Registering healthcheck: {}", name);
    self.healthchecker.add_check(name, check);
    self
  }
}

impl ServerConfigurer for Wiring {
  fn configure_server(&self, _cfg: &mut ServiceConfig) {
    debug!("Configuring healthchecks on server");
  }
}

/// Construct the wiring details
pub fn new() -> Wiring {
  debug!("Building healthchecks");

  Wiring{
    healthchecker: Healthchecker::new(),
  }
}
