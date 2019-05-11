/// Trait that represents anything that is capable of checking the health of some part of the system
pub trait Healthcheck: Send + Sync {
  /// Check the health of this component
  fn check_health(&self) -> Result<String, String>;
}

#[cfg(test)]
impl Healthcheck for Result<String, String> {
  fn check_health(&self) -> Result<String, String> {
    self.clone()
  }
}
