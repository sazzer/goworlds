package uk.co.grahamcox.goworlds.service.http.sorts

import java.lang.RuntimeException

/**
 * Exception indicating that some sort fields were unknown
 */
class UnknownSortKeyException(val unknownSorts: List<String>) : RuntimeException("Specified sort fields were unknown: $unknownSorts")
