use std::collections::HashMap;
use std::sync::Arc;
use super::Healthcheck;

// The component that is able to check the overall health of the system
pub struct Healthchecker {
  // The checks that are to be performed
  checks: HashMap<String, Arc<Healthcheck>>,
}

impl Healthchecker {
  // Create a new empty Healthchecker
  pub fn new(checks: HashMap<String, Arc<Healthcheck>>) -> Self {
    Healthchecker {
      checks: checks,
    }
  }

  // Actually perform all of the healthchecks and return the collected results
  pub fn check_health(&self) -> HashMap<String, Result<String, String>> {
    let mut result = HashMap::new();

    for (k, v) in self.checks.clone() {
      let check = v.check_health();
      result.insert(k, check);
    }

    result
  }
}

#[cfg(test)]
mod tests {
  use super::*;

  #[test]
  fn test_no_checks() {
    let checks = HashMap::new();
    let checker = Healthchecker::new(checks);

    let result = checker.check_health();

    let expected = HashMap::new();
    assert_eq!(expected, result);
  }

  #[test]
  fn test_passing_check() {
    let mut checks: HashMap<String, Arc<Healthcheck>> = HashMap::new();
    checks.insert("passing".to_owned(), Arc::new(Ok("Passed".to_owned())));
    let checker = Healthchecker::new(checks);

    let result = checker.check_health();

    let mut expected = HashMap::new();
    expected.insert("passing".to_owned(), Ok("Passed".to_owned()));
    assert_eq!(expected, result);
  }

  #[test]
  fn test_failing_check() {
    let mut checks: HashMap<String, Arc<Healthcheck>> = HashMap::new();
    checks.insert("failing".to_owned(), Arc::new(Err("Failed".to_owned())));
    let checker = Healthchecker::new(checks);

    let result = checker.check_health();

    let mut expected = HashMap::new();
    expected.insert("failing".to_owned(), Err("Failed".to_owned()));
    assert_eq!(expected, result);
  }

  #[test]
  fn test_mixed_check() {
    let mut checks: HashMap<String, Arc<Healthcheck>> = HashMap::new();
    checks.insert("passing".to_owned(), Arc::new(Ok("Passed".to_owned())));
    checks.insert("failing".to_owned(), Arc::new(Err("Failed".to_owned())));
    let checker = Healthchecker::new(checks);

    let result = checker.check_health();

    let mut expected = HashMap::new();
    expected.insert("passing".to_owned(), Ok("Passed".to_owned()));
    expected.insert("failing".to_owned(), Err("Failed".to_owned()));
    assert_eq!(expected, result);
  }
}
