use crate::database::Database;
use std::sync::Arc;

/// Data Access Layer for working with OAuth2 Clients
pub struct OAuth2ClientDao {
    db: Arc<Database>,
}

impl OAuth2ClientDao {
    /// Create a new instance of the OAuth2 Client DAO
    pub fn new(db: Arc<Database>) -> OAuth2ClientDao {
        OAuth2ClientDao{db}
    }
}

