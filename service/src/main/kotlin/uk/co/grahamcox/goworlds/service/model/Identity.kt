package uk.co.grahamcox.goworlds.service.model

import java.time.Instant
import java.util.*

/**
 * The identity of some model after it's been persisted
 * @property id The actual ID
 * @property version The version
 * @property created When the model was created
 * @property updated When the model was last updated
 */
data class Identity<ID : Id>(
        val id: ID,
        val version: UUID,
        val created: Instant,
        val updated: Instant
)
