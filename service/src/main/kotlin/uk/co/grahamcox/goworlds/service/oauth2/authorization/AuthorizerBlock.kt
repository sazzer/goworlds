package uk.co.grahamcox.goworlds.service.oauth2.authorization

import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import uk.co.grahamcox.goworlds.service.users.UserId

/**
 * Block handler for authorizing a request
 * @property accessToken The access token to authorize
 */
class AuthorizerBlock(private val accessToken: AccessToken) {
    /** Get whether we're authorized or not */
    var authorized: Boolean = true
        private set

    /**
     * Ensure that the request is for the given User ID
     * @param user The ID of the user the request must be for
     */
    fun sameUser(user: UserId) {
        if (user != accessToken.user) {
            authorized = false
        }
    }
}
