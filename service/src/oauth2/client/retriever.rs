use super::{OAuth2ClientID, OAuth2ClientModel};

/// Possible errors when retrieving an OAuth2 Client
#[derive(Debug, PartialEq)]
pub enum RetrieverError {
    ClientNotFound
}

/// Mechanism by which we can retrieve OAuth2 Client details
pub trait OAuth2ClientRetriever {
    /// Get the OAuth2 Client that has the given ID
    fn get_by_id(&self, id: &OAuth2ClientID) -> Result<OAuth2ClientModel, RetrieverError>;
}
