use crate::server::ServerConfigurer;
use actix_web::web;
use log::debug;
use std::collections::HashMap;
use std::sync::Arc;
use {super::http, super::Healthcheck, super::Healthchecker};

/// The details of the healthchecks for wiring the app together
pub struct Wiring {
    healthchecker: Arc<Healthchecker>,
}

impl ServerConfigurer for Wiring {
    fn configure_server(&self, cfg: &mut web::ServiceConfig) {
        debug!("Configuring healthchecks on server");

        cfg.service(web::resource("/health").route(web::get().to(http::check_health)))
            .data(self.healthchecker.clone());
    }
}

/// Construct the wiring details
pub fn new(checks: HashMap<String, Arc<Healthcheck>>) -> Wiring {
    debug!("Building healthchecks");

    Wiring {
        healthchecker: Arc::new(Healthchecker::new(checks)),
    }
}
