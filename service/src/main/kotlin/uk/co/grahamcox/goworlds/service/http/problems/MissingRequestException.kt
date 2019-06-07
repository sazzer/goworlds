package uk.co.grahamcox.goworlds.service.http.problems

import java.lang.RuntimeException

/**
 * Exception to indicate that a request body was required but not present
 */
class MissingRequestException : RuntimeException()
