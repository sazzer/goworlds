package uk.co.grahamcox.goworlds.service.oauth2.http

import java.lang.RuntimeException

/**
 * Exception to indicate that an Access Token was required but not present
 */
class MissingAccessTokenException : RuntimeException("Access Token was required but not present")
