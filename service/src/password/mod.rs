use argon2::{self, Config, ThreadMode, Variant, Version};
use rand::prelude::*;

/// Representation of the ways that passwords can fail
#[derive(Debug)]
pub enum Error {
    HashError,
}

/// Generate a hash for a password
/// # Examples
/// ```
/// # use ::goworlds_service::password::hash_password;
/// let hash = hash_password("password".to_owned());
/// println!("{:?}", hash);
/// ```
pub fn hash_password(password: String) -> Result<String, Error> {
    let mut salt = [0u8; 20];
    rand::thread_rng().fill_bytes(&mut salt[..]);
    log::trace!("Generated salt: {:?}", salt);

    let config = Config {
        variant: Variant::Argon2id,
        version: Version::Version13,
        mem_cost: 65536,
        time_cost: 10,
        lanes: 4,
        thread_mode: ThreadMode::Parallel,
        secret: &[],
        ad: &[],
        hash_length: 32,
    };

    match argon2::hash_encoded(password.as_bytes(), &salt, &config) {
        Ok(h) => {
            log::trace!("Hashed password: {}", h);
            Ok(h)
        }
        Err(e) => {
            log::warn!("Failed to hash password: {}", e);
            Err(Error::HashError)
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use spectral::prelude::*;

    #[test]
    fn test_hash() {
        crate::test::log::setup();
        let input = "Input";
        let hash = hash_password(input.to_owned());

        assert_that(&hash).is_ok();
    }

    #[test]
    fn test_hash_empty_password() {
        crate::test::log::setup();
        let input = "";
        let hash = hash_password(input.to_owned());

        assert_that(&hash).is_ok();
    }

    #[test]
    fn test_hash_twice() {
        crate::test::log::setup();
        let input = "Input";
        let hash1 = hash_password(input.to_owned());
        let hash2 = hash_password(input.to_owned());

        assert_that(&hash1).is_ok();
        assert_that(&hash2).is_ok();

        assert_that(&hash1.unwrap()).is_not_equal_to(hash2.unwrap());
    }
}
