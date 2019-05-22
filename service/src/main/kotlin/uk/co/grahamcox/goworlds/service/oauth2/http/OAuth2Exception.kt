package uk.co.grahamcox.goworlds.service.oauth2.http

import java.lang.RuntimeException

/**
 * Exception in handling an OAuth2 Request
 * @property code The error code
 * @param message The error message
 */
class OAuth2Exception(val code: ErrorCode, message: String) : RuntimeException(message)
