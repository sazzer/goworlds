use super::Healthchecker;
use actix_web::{http::StatusCode, web::Data, HttpResponse};
use serde::Serialize;
use std::collections::HashMap;
use std::sync::Arc;

/// Enumeration of the possible statuses of the system
#[derive(Debug, Serialize)]
enum Status {
    /// The healthcheck status is a success
    SUCCESS,
    /// The healthcheck status is a failure
    FAIL,
}

/// Representation of the health of a single component
#[derive(Debug, Serialize)]
struct ComponentHealth {
    /// Whether this component is healthy or not
    status: Status,
    /// The details of the component health
    detail: String,
}

/// Representation of the health of the whole system
#[derive(Debug, Serialize)]
struct SystemHealth {
    /// Whether the system is healthy or not
    status: Status,
    /// The individual component details
    details: HashMap<String, ComponentHealth>,
}

/// Actix handler to actually check the health of the system
pub fn check_health(healthchecker: Data<Arc<Healthchecker>>) -> HttpResponse {
    let health = healthchecker.check_health();

    let mut component_health = HashMap::new();
    let mut overall_status = Status::SUCCESS;
    let mut overall_status_code = StatusCode::OK;

    for (k, v) in health {
        let component = match v {
            Ok(detail) => ComponentHealth {
                status: Status::SUCCESS,
                detail: detail,
            },
            Err(detail) => {
                overall_status = Status::FAIL;
                overall_status_code = StatusCode::SERVICE_UNAVAILABLE;
                ComponentHealth {
                    status: Status::FAIL,
                    detail: detail,
                }
            }
        };

        component_health.insert(k, component);
    }

    let health = SystemHealth {
        status: overall_status,
        details: component_health,
    };

    HttpResponse::build(overall_status_code).json(health)
}

#[cfg(test)]
mod tests {
    use super::super::Healthcheck;
    use super::*;
    use crate::test::http;
    use serde_json::json;

    #[test]
    fn test_no_checks() {
        let checks = HashMap::new();
        let checker = Healthchecker::new(checks);

        let response = check_health(Data::new(Arc::new(checker)));

        assert_eq!(StatusCode::OK, response.status());
        http::assert_content_type("application/json", &response);
        http::assert_body_json(
            json!({
              "status": "SUCCESS",
              "details": {}
            }),
            &response,
        );
    }

    #[test]
    fn test_passing_check() {
        let mut checks: HashMap<String, Arc<Healthcheck>> = HashMap::new();
        checks.insert("passing".to_owned(), Arc::new(Ok("Passed".to_owned())));
        let checker = Healthchecker::new(checks);

        let response = check_health(Data::new(Arc::new(checker)));

        assert_eq!(StatusCode::OK, response.status());
        http::assert_content_type("application/json", &response);
        http::assert_body_json(
            json!({
              "status": "SUCCESS",
              "details": {
                "passing": {
                  "status": "SUCCESS",
                  "detail": "Passed"
                }
              }
            }),
            &response,
        );
    }

    #[test]
    fn test_failing_check() {
        let mut checks: HashMap<String, Arc<Healthcheck>> = HashMap::new();
        checks.insert("failing".to_owned(), Arc::new(Err("Failed".to_owned())));
        let checker = Healthchecker::new(checks);

        let response = check_health(Data::new(Arc::new(checker)));

        assert_eq!(StatusCode::SERVICE_UNAVAILABLE, response.status());
        http::assert_content_type("application/json", &response);
        http::assert_body_json(
            json!({
              "status": "FAIL",
              "details": {
                "failing": {
                  "status": "FAIL",
                  "detail": "Failed"
                }
              }
            }),
            &response,
        );
    }

    #[test]
    fn test_mixed_check() {
        let mut checks: HashMap<String, Arc<Healthcheck>> = HashMap::new();
        checks.insert("passing".to_owned(), Arc::new(Ok("Passed".to_owned())));
        checks.insert("failing".to_owned(), Arc::new(Err("Failed".to_owned())));
        let checker = Healthchecker::new(checks);

        let response = check_health(Data::new(Arc::new(checker)));

        assert_eq!(StatusCode::SERVICE_UNAVAILABLE, response.status());
        http::assert_content_type("application/json", &response);
        http::assert_body_json(
            json!({
              "status": "FAIL",
              "details": {
                "failing": {
                  "status": "FAIL",
                  "detail": "Failed"
                },
                "passing": {
                  "status": "SUCCESS",
                  "detail": "Passed"
                }
              }
            }),
            &response,
        );
    }
}
