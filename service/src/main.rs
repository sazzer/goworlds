use dotenv;
use ::goworlds_service::settings::Settings;

/// The main entrypoint to start the entire application
fn main() {
  dotenv::dotenv().ok();
  let s = Settings::new();

  println!("{:?}", s);

  ::goworlds_service::start_service().unwrap();
}
