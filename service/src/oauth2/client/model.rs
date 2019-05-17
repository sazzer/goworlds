use crate::users::UserID;
use crate::model::Model;

/// The unique ID of an OAuth2 Client
pub type OAuth2ClientID = String;

/// Enumeration of supported Response Types
#[derive(Debug)]
pub enum ResponseType {
    Code,
    Token,
    IdToken
}

/// Enumeration of supported Grant Types
#[derive(Debug)]
pub enum GrantType {
    AuthorizationCode,
    Implicit,
    ClientCredentials,
    RefreshToken,
}

/// The details of an OAuth2 Client
#[derive(Debug)]
pub struct OAuth2ClientData {
    name: String,
    secret: String,
    owner: UserID,
    redirect_uris: Vec<String>,
    response_types: Vec<ResponseType>,
    grant_types: Vec<GrantType>,
}

/// The representation of a persisten OAuth2 Client
pub type OAuth2ClientModel = Model<OAuth2ClientID, OAuth2ClientData>;
