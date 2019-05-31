package uk.co.grahamcox.goworlds.service.model

/**
 * Representation of a page of data
 */
data class Page<DATA>(
        val offset: Long,
        val total: Long,
        val entries: List<DATA>
) {
    /** Whether we have a next page */
    val hasNext = (offset + entries.size) < total

    /** Whether we have a previous page */
    val hasPrevious = (offset > 0)

    /** Whether this is the first page - i.e. we don't have a previous page */
    val isFirstPage = !hasPrevious

    /** Whether this is the last page - i.e. we don't have a next page */
    val isLastPage = !hasNext
}
