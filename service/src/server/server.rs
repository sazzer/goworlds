use actix_web::{middleware, middleware::cors, App, HttpServer, web};
use std::io::Result;

// Server represents the actual HTTP Server to run
pub struct Server {
  port: u16,
}

fn index() -> String {
    "Hello!".to_owned()
}

fn poster() -> String {
    "Goodbye!".to_owned()
}

trait RouteRegister: Sync + Send {
  fn register_route(&self, cfg: &mut web::ServiceConfig);
}

struct IndexRouteRegister{}
impl RouteRegister for IndexRouteRegister {
  fn register_route(&self, cfg: &mut web::ServiceConfig) {
    cfg.route("/", web::get().to(index));
    cfg.route("/", web::post().to(poster));
  }
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

    let handlers: Vec<std::sync::Arc<RouteRegister>> = vec![
      std::sync::Arc::new(IndexRouteRegister{})
    ];

    HttpServer::new(move || {
      App::new()
        .wrap(middleware::Logger::default())
        .wrap(cors::Cors::new())
        .configure(|cfg| {
          let hn = handlers.clone();
          for h in hn {
            h.register_route(cfg);
          }
        })
    })
    .bind(address)?
    .run()
  }
}
