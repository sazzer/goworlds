use postgres::error::SqlState;

/// Representation of a single row returned from a query
pub struct Row {
}

impl Row {
    /// Get the number of columns returned
    pub fn len(&self) -> usize {
        self.column_names().len()
    }

    // Get the list of column names
    pub fn column_names(&self) -> &[String] {
        &[]
    }
}

/// Error returned from any calls to the database
#[derive(Clone, Debug, PartialEq)]
pub struct Error {
    pub code: Option<SqlState>,
}

impl Error {
    /// Construct a new error with no error code
    pub fn new() -> Self {
        Error{
            code: None
        }
    }

    /// Construct a new error with a Postgres error code
    pub fn new_from_code(code: SqlState) -> Self {
        Error{
            code: Some(code)
        }
    }
}

impl std::fmt::Display for Error {
    /// Format the error for printing
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        write!(f, "Error with query: {:?}", self.code)
    }
}

impl std::error::Error for Error {
    /// Get the error description
    fn description(&self) -> &str {
        "Error with query"
    }

    /// Get the root cause. Never used
    fn cause(&self) -> Option<&std::error::Error> {
        // Generic error, underlying cause isn't tracked.
        None
    }
}

pub trait Database {}
