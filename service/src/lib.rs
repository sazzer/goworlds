#![allow(dead_code)]

use std::collections::HashMap;
use std::io::Result;
use std::sync::Arc;
use log::info;

pub mod settings;
pub mod server;
pub mod health;
#[cfg(test)]
pub mod test;

fn register_index(_: &mut actix_web::web::ServiceConfig) {
  info!("Registering route in free function");
}

// Actually build and start the service running, using the provided settings
pub fn start_service(config: settings::Settings) -> Result<()> {
  let healthchecks = HashMap::new();
  let health_wiring = health::wiring::new(healthchecks);

  server::Server::new(config.port)
    .add_configuration(Arc::new(health_wiring))
    .add_configuration(Arc::new(register_index))
    .add_configuration(Arc::new(|_: &mut actix_web::web::ServiceConfig| {
      info!("Registering route in closure");
    }))
    .run()
}
