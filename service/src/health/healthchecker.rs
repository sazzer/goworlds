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
  pub fn new() -> Self {
    Healthchecker {
      checks: HashMap::new(),
    }
  }

  // Add a new check to be performed
  pub fn add_check(&mut self, name: String, check: Arc<Healthcheck>) -> &mut Self {
    self.checks.insert(name, check);
    self
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
    let checker = Healthchecker::new();

    let result = checker.check_health();

    let expected = HashMap::new();
    assert_eq!(expected, result);
  }

  #[test]
  fn test_passing_check() {
    let mut checker = Healthchecker::new();
    checker.add_check("passing".to_owned(), Arc::new(Ok("Passed".to_owned())));

    let result = checker.check_health();

    let mut expected = HashMap::new();
    expected.insert("passing".to_owned(), Ok("Passed".to_owned()));
    assert_eq!(expected, result);
  }

  #[test]
  fn test_failing_check() {
    let mut checker = Healthchecker::new();
    checker.add_check("failing".to_owned(), Arc::new(Err("Failed".to_owned())));

    let result = checker.check_health();

    let mut expected = HashMap::new();
    expected.insert("failing".to_owned(), Err("Failed".to_owned()));
    assert_eq!(expected, result);
  }

  #[test]
  fn test_mixed_check() {
    let mut checker = Healthchecker::new();
    checker.add_check("passing".to_owned(), Arc::new(Ok("Passed".to_owned())));
    checker.add_check("failing".to_owned(), Arc::new(Err("Failed".to_owned())));

    let result = checker.check_health();

    let mut expected = HashMap::new();
    expected.insert("passing".to_owned(), Ok("Passed".to_owned()));
    expected.insert("failing".to_owned(), Err("Failed".to_owned()));
    assert_eq!(expected, result);
  }
}
