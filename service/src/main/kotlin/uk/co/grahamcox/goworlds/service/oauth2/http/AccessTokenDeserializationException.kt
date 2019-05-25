package uk.co.grahamcox.goworlds.service.oauth2.http

import java.lang.RuntimeException

/**
 * Exception to indicate that deserializing the access token failed
 */
class AccessTokenDeserializationException(val token: String) : RuntimeException("Failed to deserialize Access Token $token")
