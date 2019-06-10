package uk.co.grahamcox.goworlds.service.oauth2.authorization

import java.lang.RuntimeException

/**
 * Exception to indicate that authorization failed for a request
 */
class AuthorizationFailedException : RuntimeException("Authorization failed")
