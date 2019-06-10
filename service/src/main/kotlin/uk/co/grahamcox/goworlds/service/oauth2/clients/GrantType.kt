package uk.co.grahamcox.goworlds.service.oauth2.clients

/**
 * Enumeration of supported OAuth2 Grant Types
 */
enum class GrantType {
    AUTHORIZATION_CODE,
    IMPLICIT,
    CLIENT_CREDENTIALS,
    REFRESH_TOKEN,
    EMAIL_PASSWORD_EXTENSION
}
