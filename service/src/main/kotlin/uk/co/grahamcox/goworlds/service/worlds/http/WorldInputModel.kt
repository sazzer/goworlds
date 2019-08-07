package uk.co.grahamcox.goworlds.service.worlds.http

/**
 * HTTP API Model for creating or updating a world
 */
data class WorldInputModel(
        val name: String?,
        val description: String?,
        val slug: String?
)
