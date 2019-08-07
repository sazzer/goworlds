package uk.co.grahamcox.goworlds.service.worlds.http

import java.time.Instant

/**
 * Representation of a World as returned over the API
 */
data class WorldModel(
        val id: String,
        val created: Instant,
        val updated: Instant,
        val name: String,
        val description: String,
        val slug: String,
        val owner: String
)
