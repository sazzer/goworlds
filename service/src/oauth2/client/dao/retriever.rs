use super::OAuth2ClientDao;
use crate::oauth2::client::*;

impl OAuth2ClientRetriever for OAuth2ClientDao {
    /// Get the OAuth2 Client that has the given ID
    fn get_by_id(&self, id: &OAuth2ClientID) -> Result<OAuth2ClientModel, RetrieverError> {
        log::debug!("Getting OAuth2 Client with ID: {}", id);
        Err(RetrieverError::ClientNotFound)
    }
}
