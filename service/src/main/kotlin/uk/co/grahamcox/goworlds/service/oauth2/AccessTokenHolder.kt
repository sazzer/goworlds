package uk.co.grahamcox.goworlds.service.oauth2

import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken

/**
 * Mechanism to hold the access token for the current request
 */
interface AccessTokenHolder {
    /**
     * The access token for the current request, or null if there isn't one
     */
    val accessToken: AccessToken?
}
