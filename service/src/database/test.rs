use postgres::types::ToSql;
use std::collections::VecDeque;
use std::rc::Rc;

#[derive(Debug, PartialEq)]
enum ExpectedOutcome {
    None,
    Error { error: super::Error },
    ExecCount { count: u64 },
}

#[derive(Debug)]
pub struct Expectation {
    sql: String,
    params: Vec<Rc<ToSql>>,
    result: ExpectedOutcome,
}

pub struct Database {
    expectations: VecDeque<Expectation>,
}

impl Database {
    fn new() -> Database {
        Database {
            expectations: VecDeque::new(),
        }
    }

    fn expect(&mut self, sql: &str) -> &mut Expectation {
        self.expectations.push_back(Expectation {
            sql: sql.to_owned(),
            params: Vec::new(),
            result: ExpectedOutcome::None
        });

        self.expectations.front_mut().unwrap()
    }
}

impl Expectation {
    fn with_param(&mut self, param: Rc<ToSql>) -> &mut Self {
        self.params.push(param.clone());
        self
    }

    fn returns_error(&mut self, err: super::Error) -> &mut Self {
        self.result = ExpectedOutcome::Error {error: err};
        self
    }

    fn returns_exec_count(&mut self, count: u64) -> &mut Self {
        self.result = ExpectedOutcome::ExecCount {count: count};
        self
    }
}

impl super::Database for Database {}

#[cfg(test)]
mod tests {
    use super::*;
    use spectral::prelude::*;

    fn params_match(actual: &Vec<Rc<ToSql>>, expected: &Vec<Rc<ToSql>>) {
        assert_that(&actual.len()).named("Number of params").is_equal_to(expected.len());

        let it = actual.iter().zip(expected.iter());

        for (i, (a, e)) in it.enumerate() {
            let expected_param = format!("{:?}", e);
            let actual_param = format!("{:?}", a);
            let name = format!("Param {}", i);

            assert_that(&actual_param).named(&name).is_equal_to(expected_param);
        }

    }

    #[test]
    fn test_expect_sql_no_params() {
        let mut db = Database::new();
        let expectation = db.expect("SELECT * FROM users");

        assert_that(&expectation.sql).is_equal_to("SELECT * FROM users".to_owned());
        params_match(&expectation.params, &vec![]);
        assert_that(&expectation.result).is_equal_to(ExpectedOutcome::None);
    }

    #[test]
    fn test_expect_sql_params() {
        let mut db = Database::new();
        let expectation = db.expect("SELECT * FROM users WHERE user_id = $1")
            .with_param(Rc::new("123"));

        assert_that(&expectation.sql).is_equal_to("SELECT * FROM users WHERE user_id = $1".to_owned());
        params_match(&expectation.params, &vec![Rc::new("123")]);
        assert_that(&expectation.result).is_equal_to(ExpectedOutcome::None);
    }

    #[test]
    fn test_expect_sql_multiple_params() {
        let mut db = Database::new();
        let expectation = db.expect("SELECT * FROM users WHERE user_id = $1 OR user_id = $2")
            .with_param(Rc::new("123"))
            .with_param(Rc::new("432"));

        assert_that(&expectation.sql).is_equal_to("SELECT * FROM users WHERE user_id = $1 OR user_id = $2".to_owned());
        params_match(&expectation.params, &vec![Rc::new("123"), Rc::new("432")]);
        assert_that(&expectation.result).is_equal_to(ExpectedOutcome::None);
    }

    #[test]
    fn test_expect_error() {
        let mut db = Database::new();
        let expectation = db.expect("SELECT * FROM users")
            .returns_error(crate::database::Error::new());

        assert_that(&expectation.sql).is_equal_to("SELECT * FROM users".to_owned());
        params_match(&expectation.params, &vec![]);
        assert_that(&expectation.result).is_equal_to(ExpectedOutcome::Error {
            error: crate::database::Error::new()
        });
    }

    #[test]
    fn test_expect_error_with_code() {
        let mut db = Database::new();
        let expectation = db.expect("SELECT * FROM users")
            .returns_error(crate::database::Error::new_from_code(postgres::error::SqlState::TOO_MANY_CONNECTIONS));

        assert_that(&expectation.sql).is_equal_to("SELECT * FROM users".to_owned());
        params_match(&expectation.params, &vec![]);
        assert_that(&expectation.result).is_equal_to(ExpectedOutcome::Error {
            error: crate::database::Error::new_from_code(postgres::error::SqlState::TOO_MANY_CONNECTIONS),
        });
    }

    #[test]
    fn test_expect_exec_count() {
        let mut db = Database::new();
        let expectation = db.expect("DELETE * FROM users")
            .returns_exec_count(10);

        assert_that(&expectation.sql).is_equal_to("DELETE * FROM users".to_owned());
        params_match(&expectation.params, &vec![]);
        assert_that(&expectation.result).is_equal_to(ExpectedOutcome::ExecCount {count: 10});
    }
}
