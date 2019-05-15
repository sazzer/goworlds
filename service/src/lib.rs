#![allow(dead_code)]

pub mod database;
pub mod health;
pub mod password;
pub mod server;
pub mod settings;
pub mod model;
pub mod users;
pub mod oauth2;
#[cfg(test)]
pub mod test;

use log::info;
use std::collections::HashMap;
use std::io::Result;
use std::sync::Arc;

fn register_index(_: &mut actix_web::web::ServiceConfig) {
    info!("Registering route in free function");
}

// Actually build and start the service running, using the provided settings
pub fn start_service(config: settings::Settings) -> Result<()> {
    let database = Arc::new(database::DatabaseWrapper::new(config.database_uri).unwrap());
    database.migrate().unwrap();

    let mut healthchecks: HashMap<String, Arc<health::Healthcheck>> = HashMap::new();
    healthchecks.insert("database".to_owned(), database);
    let health_wiring = health::wiring::new(healthchecks);

    server::Server::new(config.port)
        .add_configuration(Arc::new(health_wiring))
        .add_configuration(Arc::new(register_index))
        .add_configuration(Arc::new(|_: &mut actix_web::web::ServiceConfig| {
            info!("Registering route in closure");
        }))
        .run()
}
