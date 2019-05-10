#[macro_use]
extern crate log;

use dotenv;
use ::goworlds_service::settings::Settings;
use log4rs;

/// The main entrypoint to start the entire application
fn main() {
  dotenv::dotenv().ok();
  let s = Settings::new().unwrap();

  log4rs::init_file("log4rs.yml", Default::default()).unwrap();

  info!("Starting Service...");
  ::goworlds_service::start_service(s).unwrap();
}
