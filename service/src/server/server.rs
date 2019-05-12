use actix_web::{middleware, middleware::cors, web::ServiceConfig, App, HttpServer};
use std::io::Result;
use std::sync::Arc;

/// Trait that represents how we can configure the server
pub trait ServerConfigurer: Sync + Send {
    fn configure_server(&self, cfg: &mut ServiceConfig);
}

/// Helper Implementation of ServerConfigurer for free functions
impl<F> ServerConfigurer for F
where
    F: Sync + Send + Fn(&mut ServiceConfig),
{
    fn configure_server(&self, cfg: &mut ServiceConfig) {
        self(cfg);
    }
}

/// Server represents the actual HTTP Server to run
pub struct Server {
    port: u16,
    config: Vec<Arc<ServerConfigurer>>,
}

impl Server {
    /// Create a new instance of the server
    pub fn new(port: u16) -> Self {
        Server {
            port: port,
            config: Vec::new(),
        }
    }

    /// Add a single configuraton method to the server.
    /// Configuration is anything that implements the ServerConfigurer Trait. By default, all Functions with the correct
    /// signature automatically do this, and the trait can be implemented by anything else as is appropriate.
    ///
    /// # Examples
    ///
    /// Registering a Free Function
    /// ```
    /// # use std::sync::Arc;
    /// # use ::goworlds_service::server::Server;
    /// fn free_function(_: &mut actix_web::web::ServiceConfig) {}
    /// Server::new(3000)
    ///   .add_configuration(Arc::new(free_function));
    /// ```
    ///
    /// Registering a Closure
    /// ```
    /// # use std::sync::Arc;
    /// # use ::goworlds_service::server::Server;
    /// Server::new(3000)
    ///   .add_configuration(Arc::new(|_: &mut actix_web::web::ServiceConfig| {
    ///   }));
    /// ```
    ///
    /// Registering a Struct
    /// ```
    /// # use std::sync::Arc;
    /// # use ::goworlds_service::server::{Server, ServerConfigurer};
    /// struct RegistrationStruct {}
    /// impl ServerConfigurer for RegistrationStruct {
    ///     fn configure_server(&self, _: &mut actix_web::web::ServiceConfig) {}
    /// }
    /// Server::new(3000)
    ///   .add_configuration(Arc::new(RegistrationStruct{}));
    /// ```
    pub fn add_configuration(&mut self, cfg: Arc<ServerConfigurer>) -> &mut Self {
        self.config.push(cfg);
        self
    }

    /// Actually start the server running
    pub fn run(&self) -> Result<()> {
        let address = format!("[::]:{}", self.port);

        let configurations = self.config.clone();

        HttpServer::new(move || {
            App::new()
                .wrap(middleware::Logger::default())
                .wrap(cors::Cors::new())
                .configure(|cfg| {
                    let local_configurations = configurations.clone();
                    for c in local_configurations {
                        c.configure_server(cfg);
                    }
                })
        })
        .bind(address)?
        .run()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_create_server() {
        Server::new(3000);
    }

    #[test]
    fn test_register_closure() {
        Server::new(3000).add_configuration(Arc::new(|_: &mut ServiceConfig| {}));
    }

    fn register_free_function(_: &mut ServiceConfig) {}

    #[test]
    fn test_register_free_function() {
        Server::new(3000).add_configuration(Arc::new(register_free_function));
    }

    struct RegistrationStruct {}
    impl ServerConfigurer for RegistrationStruct {
        fn configure_server(&self, _: &mut ServiceConfig) {}
    }

    #[test]
    fn test_register_struct() {
        Server::new(3000).add_configuration(Arc::new(RegistrationStruct {}));
    }
}
