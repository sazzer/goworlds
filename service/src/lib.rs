use actix_web::{middleware, web, App, HttpServer};
use std::sync::Arc;

trait UserRetriever {
  fn get_user_name(&self) -> String;
}

trait UserUpdater {
  fn get_user_age(&self) -> String;
}

struct UserDao {

}

impl UserRetriever for UserDao {
  fn get_user_name(&self) -> String {
    "Graham".to_owned()
  }
}

impl UserUpdater for UserDao {
  fn get_user_age(&self) -> String {
    "36".to_owned()
  }
}

fn index(user_retriever: web::Data<Arc<UserRetriever>>, user_updater: web::Data<Arc<UserUpdater>>) -> String {
  let mut result = "".to_owned();
  result.push_str(&user_retriever.get_user_name());
  result.push_str(" ");
  result.push_str(&user_updater.get_user_age());

  result
}

pub fn start_service() -> std::io::Result<()> {
  let user_dao = Arc::new(UserDao{});

  HttpServer::new(move || {
      App::new()
        .data::<Arc<UserRetriever>>(user_dao.clone())
        .data::<Arc<UserUpdater>>(user_dao.clone())
        // enable logger
        .wrap(middleware::Logger::default())
        .service(web::resource("/").to(index))
  })
  .bind("127.0.0.1:8080")?
  .run()
}
