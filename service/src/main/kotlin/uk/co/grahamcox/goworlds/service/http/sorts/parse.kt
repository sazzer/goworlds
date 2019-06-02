package uk.co.grahamcox.goworlds.service.http.sorts

import org.slf4j.LoggerFactory
import uk.co.grahamcox.goworlds.service.model.Sort
import uk.co.grahamcox.goworlds.service.model.SortDirection
import java.lang.IllegalArgumentException

val LOG = LoggerFactory.getLogger("uk.co.grahamcox.goworlds.service.http.sorts.parseSorts")!!

/**
 * Parse a string that represents a list of sorts
 */
inline fun <reified T : Enum<T>> parseSorts(input: String) : List<Sort<T>> {
    val sortClass = T::class.java

    if (!sortClass.isEnum) {
        throw IllegalArgumentException("Sort class must be an Enum")
    }

    val enumConstants = sortClass.enumConstants
            .map { it.name.toUpperCase() to it }
            .toMap()

    LOG.debug("Enum constants for class {}: {}", sortClass, enumConstants)

    val parsedSorts = input.split(",")
            .asSequence()
            .map(String::trim)
            .filter { it.isNotBlank() }
            .map {
                when (it[0]) {
                    '+' -> it.substring(1) to SortDirection.ASCENDING
                    '-' -> it.substring(1) to SortDirection.DESCENDING
                    else -> it to SortDirection.ASCENDING
                }
            }
            .toList()

    val unknownSorts = parsedSorts.filter { (key, _) ->
        !enumConstants.containsKey(key.toUpperCase().replace(' ', '_'))
    }.map { it.first }

    if (unknownSorts.isNotEmpty()) {
        LOG.warn("Unknown sort keys {} for sort class {}", unknownSorts, sortClass)
        throw UnknownSortKeyException(unknownSorts)
    }

    val result = parsedSorts
            .map { (key, direction) ->
                key.toUpperCase().replace(' ', '_') to direction
            }
            .map { (key, direction) -> enumConstants.getValue(key) to direction }
            .map { (key, direction) -> Sort(key, direction) }
            .toList()
    LOG.debug("Parsed input string {} for sort class {} into {}", input, sortClass, result)
    return result
}
