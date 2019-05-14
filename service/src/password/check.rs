use argon2::verify_encoded;

/// Check that the given plaintext password and the provided hash correspond to each other
/// # Examples
/// ```
/// # use ::goworlds_service::password::check_password;
/// # let hash = "HashedPassword".to_owned();
/// let result = check_password("password".to_owned(), hash);
/// ```
pub fn check_password(password: String, hash: String) -> bool {
    match verify_encoded(&hash, password.as_bytes()) {
        Ok(true) => true,
        Ok(false) => false,
        Err(e) => {
            log::warn!("Failed to check password: {}", e);
            false
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use super::super::hash_password;
    use spectral::prelude::*;

    #[test]
    fn test_correct_hash() {
        crate::test::log::setup();
        let pw = "Input";
        let hash = hash_password(pw.to_owned()).unwrap();

        let result = check_password(pw.to_owned(), hash);
        assert_that(&result).is_equal_to(true);
    }

    #[test]
    fn test_incorrect_hash() {
        crate::test::log::setup();
        let hash = hash_password("Input".to_owned()).unwrap();

        let result = check_password("Other".to_owned(), hash);
        assert_that(&result).is_equal_to(false);
    }

    #[test]
    fn test_invalid_hash() {
        crate::test::log::setup();
        let result = check_password("Input".to_owned(), "Invalid".to_owned());
        assert_that(&result).is_equal_to(false);
    }
}
