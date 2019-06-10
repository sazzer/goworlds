package uk.co.grahamcox.goworlds.service.oauth2.authorization

import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken

/**
 * Authorizer for authorizing a request
 * @property token The access token to authorize
 */
class Authorizer(private val token: AccessToken) {
    /**
     * Invoke the authorizer with the given lambda
     * @param block The lambda to use
     */
    operator fun invoke(block: AuthorizerBlock.() -> Unit) {
        val authorizerBlock = AuthorizerBlock(token)

        block.invoke(authorizerBlock)

        if (!authorizerBlock.authorized) {
            throw AuthorizationFailedException()
        }
    }
}
