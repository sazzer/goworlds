package uk.co.grahamcox.goworlds.service.http

import uk.co.grahamcox.goworlds.service.http.problems.InvalidCountException
import uk.co.grahamcox.goworlds.service.http.problems.InvalidOffsetException
import uk.co.grahamcox.goworlds.service.http.sorts.parseSorts
import uk.co.grahamcox.goworlds.service.model.Sort

/**
 * Representation of the standard search fields for HTTP APIs
 */
data class SearchFields<SORT>(val offset: Long, val count: Long, val sorts: List<Sort<SORT>>)

/**
 * Parse the given parameters into the standard search fields
 * @param offset The offset to parse
 * @param count The count to parse
 * @param sorts The sorts to parse
 * @return the parsed search fields
 */
inline fun <reified SORT : Enum<SORT>> parseSearchFields(offset: String?, count: String?, sorts: String?) : SearchFields<SORT> {
    val parsedOffset = try {
        offset?.toLong() ?: 0
    } catch (e: NumberFormatException) {
        throw InvalidOffsetException()
    }
    if (parsedOffset < 0) throw InvalidOffsetException()

    val parsedCount = try {
        count?.toLong() ?: 10
    } catch (e: NumberFormatException) {
        throw InvalidCountException()
    }
    if (parsedCount < 0) throw InvalidCountException()

    val parsedSorts = sorts?.let { parseSorts<SORT>(it) } ?: emptyList()

    return SearchFields(parsedOffset, parsedCount, parsedSorts)
}