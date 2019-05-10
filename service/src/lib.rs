use std::io::Result;

pub mod settings;
mod server;

// Actually build and start the service running, using the provided settings
pub fn start_service(config: settings::Settings) -> Result<()> {
  let server = server::Server::new(config.port);

  server.run()
}
