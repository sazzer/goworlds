package uk.co.grahamcox.goworlds.service.http.problems

import java.lang.RuntimeException

/**
 * Exception to indicate that a request field was required but not present
 */
class MissingRequestFieldException(val field: String) : RuntimeException("Required request field not present: $field")
