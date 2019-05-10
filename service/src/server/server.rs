use actix_web::{middleware, middleware::cors, App, HttpServer};
use std::io::Result;

// Server represents the actual HTTP Server to run
pub struct Server {
  port: u16,
}

impl Server {
  // Create a new instance of the server
  pub fn new(port: u16) -> Self {
    Server{
      port: port,
    }
  }

  // Actually start the server running
  pub fn run(&self) -> Result<()> {
    let address = format!("[::]:{}", self.port);

    HttpServer::new(move || {
      App::new()
        .wrap(middleware::Logger::default())
        .wrap(cors::Cors::new())
    })
    .bind(address)?
    .run()
  }
}
