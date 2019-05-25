package uk.co.grahamcox.goworlds.service.oauth2.http

import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import uk.co.grahamcox.goworlds.service.oauth2.AccessTokenHolder
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken

/**
 * Store of access tokens.
 * Basically a wrapper around {@link RequestAttributes} for storing the Access Token
 */
class AccessTokenStore : AccessTokenHolder {
    companion object {
        /** The name of the request attribute to store the access token in */
        private val ATTRIBUTE_NAME = AccessToken::class.java.name
        /** The scope of the request attribute to store the access token in */
        private const val ATTRIBUTE_SCOPE = RequestAttributes.SCOPE_REQUEST
    }

    /** The attributes of the current request */
    private val currentRequestAttributes
        get() = RequestContextHolder.currentRequestAttributes()

    /**
     * The access token for the current request, or null if there isn't one
     */
    override var accessToken: AccessToken?
        get() = currentRequestAttributes.getAttribute(ATTRIBUTE_NAME, ATTRIBUTE_SCOPE) as AccessToken?
        set(token) = when (token) {
            null -> {
                currentRequestAttributes.removeAttribute(ATTRIBUTE_NAME, ATTRIBUTE_SCOPE)
            }
            else -> currentRequestAttributes.setAttribute(ATTRIBUTE_NAME, token, ATTRIBUTE_SCOPE)
        }
}
