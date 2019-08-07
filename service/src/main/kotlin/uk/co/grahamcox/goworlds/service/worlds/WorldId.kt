package uk.co.grahamcox.goworlds.service.worlds

import uk.co.grahamcox.goworlds.service.model.Id
import java.util.*

/**
 * The ID of a World
 */
data class WorldId(override val id: UUID) : Id