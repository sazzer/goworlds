package uk.co.grahamcox.goworlds.service.worlds

import uk.co.grahamcox.goworlds.service.users.UserId

/**
 * The data that makes up a World
 * @property name The name of the World
 * @property owner The ID of the user that owns the World
 * @property description The description of the World
 * @property slug The URL Slug for the world
 */
data class WorldData(
        val name: String,
        val owner: UserId,
        val description: String,
        val slug: String
)
