use dotenv;

fn main() {
  dotenv::dotenv().ok();

  ::goworlds_service::start_service().unwrap();
}
