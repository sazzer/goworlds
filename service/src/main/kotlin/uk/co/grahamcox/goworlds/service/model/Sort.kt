package uk.co.grahamcox.goworlds.service.model

/**
 * Representation of a sort field
 */
data class Sort<T>(
        val field: T,
        val direction: SortDirection
)
