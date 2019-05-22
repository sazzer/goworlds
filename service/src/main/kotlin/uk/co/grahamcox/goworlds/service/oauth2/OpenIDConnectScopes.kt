package uk.co.grahamcox.goworlds.service.oauth2

/**
 * Scopes defined in the OpenID Connect specification.
 * These affect the claims that are present in the id_token if one is returned.
 */
enum class OpenIDConnectScopes(override val id: String) : Scope{
    OPENID("openid"),
    PROFILE("profile"),
    EMAIL("email")
}
