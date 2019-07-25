package uk.co.grahamcox.goworlds.service.worlds

import java.util.*

/**
 * Possible filters for searching worlds
 */
data class WorldSearchFilters(
        val name: String? = null,
        val owner: UUID? = null
)
